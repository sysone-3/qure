<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="ko_KR" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>대시보드</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/assets/css/dashboard.css?after'/>"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="container">
    <!-- 상단 3열 -->
    <div class="grid">
        <!-- 인사 카드 -->
        <section class="card greet">
            <div>
                <h1 class="title">안녕하세요, <span style="color:#828282">${vm.name}</span>님</h1>
                <div class="email">${vm.email}</div>
                <p class="info">대시보드에서 오늘의 점검 현황을 확인해보세요.</p>
            </div>
            <div>
                <span class="pill"><span class="num">${todayStats.todayTotal}</span> 오늘의 점검</span>
                <span class="pill"><span class="num">${todayStats.todayCompleted}</span> 완료된 점검</span>
            </div>
        </section>

        <!-- 우측 -->
        <section class="card list" style="grid-column: span 2;">
            <div class="kpi">
                <div class="card-kpi">
                    <h4 style="font-weight: 600;">전체 점검 완료율</h4>
                    <c:set var="totalAll" value="${otherStats.untilYesterdayTotal + todayStats.todayTotal}" />
                    <c:set var="completedAll" value="${otherStats.untilYesterdayCompleted + todayStats.todayCompleted}" />
                    <c:set var="overallPct" value="${totalAll == 0 ? 100 : (completedAll * 100) / totalAll}" />
                    <div class="big">
                        <fmt:formatNumber value="${overallPct}" pattern="#.#" />%
                    </div>
                </div>
                <div class="card-kpi">
                    <h4 style="font-weight: 600;">예정되어 있는 점검</h4>
                    <div class="big" style="color:#111">${otherStats.upcomingCount}</div>
                </div>
                <div class="card-kpi">
                    <h4 style="font-weight: 600;">미점검 현황</h4>
                    <div class="big" style="color:#ef4444">${otherStats.overdueUninspected}</div>
                </div>
                <div class="card-kpi">
                    <h4 style="font-weight: 600;">접수된 민원</h4>
                    <div class="big" style="color:#111">${otherStats.openComplaints}</div>
                </div>
            </div>
        </section>
    </div>

    <!-- 최근 점검 현황 -->
    <section class="card ring-row" style="margin-top:22px">
        <h3 style="margin:0 0 16px 0;font-size:22px;font-weight:800">최근 점검 현황</h3>

        <div class="rings">
            <c:forEach var="p" items="${recentPoints}" varStatus="s">
                <fmt:formatDate value="${p.dayValue}" pattern="d" var="dnum"/>
                <canvas id="ring${s.index}" class="ring"
                        width="72" height="72" style="width:72px;height:72px"
                        data-percent="${p.percent}" data-day="${dnum}"></canvas>
            </c:forEach>
        </div>
    </section>

    <section class="card">
        <div style="display: flex; justify-content: space-between">
            <h3 style="margin:0 0 16px 0;font-size:22px;font-weight:800">최근 점검 내역</h3>
            <span>전체 보기</span>
        </div>
        <table class="table">
            <thead>
            <tr>
                <th>유형</th>
                <th>점검 설비</th>
                <th>점검일</th>
                <th>점검자</th>
                <th>점검 상태</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="r" items="${recentInspections}">
                <tr>
                    <td>${r.domain}</td>
                    <td>
                        <c:out value="${r.facilityName}" />
                        <c:if test="${not empty r.floor}"> ${r.floor}층</c:if>
                        <c:if test="${not empty r.zone}"> ${r.zone}구역</c:if>
                    </td>
                    <td><fmt:formatDate value="${r.submittedAt}" pattern="yyyy년 M월 d일" /></td>
                    <td>${r.inspectorName}</td>
                    <td>${r.result}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</div>

<script>
    (function () {
        if (!window.Chart) { console.error("Chart.js not loaded"); return; }

        // 중앙 텍스트 플러그인
        const CenterText = {
            id: 'centerText',
            afterDraw(chart) {
                const meta = chart.getDatasetMeta(0);
                if (!meta?.data?.[0]) return;
                const {ctx} = chart, {x, y} = meta.data[0];
                const label = chart.config.options.plugins.centerText?.label ?? '';
                ctx.save();
                ctx.textAlign = 'center'; ctx.textBaseline = 'middle';
                ctx.font = 'bold 16px system-ui'; ctx.fillStyle = '#111';
                ctx.fillText(label, x, y);
                ctx.restore();
            }
        };
        Chart.register(CenterText);

        // 각 캔버스 → 차트 생성
        document.querySelectorAll('canvas.ring').forEach((el) => {
            const raw = Number(el.dataset.percent);
            const pctClamped = Number.isFinite(raw) ? Math.max(0, Math.min(100, Math.floor(raw))) : 0;
            const day = el.dataset.day || '';
            const ctx = el.getContext('2d');

            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    datasets: [{
                        data: [pctClamped, 100 - pctClamped],
                        backgroundColor: ['#FFD966', '#E5E7EB'],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: false,
                    maintainAspectRatio: false,
                    rotation: -Math.PI / 2, // 12시 방향 시작
                    animation: { animateRotate: true, duration: 900 },
                    cutout: (pctClamped === 100 ? '0%' : '70%'), // 100%면 꽉 찬 원
                    plugins: {
                        legend: { display: false },
                        tooltip:{ enabled: false },
                        centerText: { label: day }
                    }
                }
            });
        });
    })();
</script>
</body>
</html>
