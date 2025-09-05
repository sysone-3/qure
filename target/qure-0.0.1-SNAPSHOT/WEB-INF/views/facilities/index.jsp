<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 목록</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        :root{
            --bg:#fff7f0;            /* 전체 배경 톤 */
            --panel:#ffffff;
            --line:#f0e6dc;
            --text:#3c3c3c;
            --muted:#a8a29e;
            --brand:#f7d58a;         /* 상단 헤더 노란색 */
            --brand-strong:#f2c65b;
            --pill:#f6f6f6;
            --danger:#ef4444;
            --ok:#22c55e;
            --warn:#f59e0b;
        }
        *{box-sizing:border-box}
        body{
            margin:0; background:var(--bg); color:var(--text); font:14px/1.5 "Pretendard",system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;
        }
        .wrap{max-width:1080px; margin:24px auto; padding:16px}
        .card{
            background:var(--panel); border-radius:16px; box-shadow:0 6px 20px rgba(0,0,0,.05); overflow:hidden;
        }
        .toolbar{
            display:flex; gap:12px; align-items:center; padding:16px;
        }
        .search{
            position:relative; flex:1;
        }
        .search input{
            width:100%; border:1px solid var(--line); border-radius:999px; padding:12px 40px 12px 44px; background:#fff;
            outline:none;
        }
        .search svg{ position:absolute; left:14px; top:50%; transform:translateY(-50%); width:18px; height:18px; opacity:.6; }
        .btn{
            appearance:none; border:1px solid var(--line); background:#fff; padding:10px 14px; border-radius:12px; cursor:pointer;
        }
        .btn-ghost{ background:#fff; }
        .btn-brand{
            background:var(--brand); border-color:var(--brand-strong); font-weight:600;
        }
        .table{
            width:100%; border-collapse:separate; border-spacing:0; width:100%;
        }
        .thead{
            background:var(--brand); color:#5a4a21; font-weight:700;
        }
        .thead th{
            padding:14px 16px; text-align:left; font-size:13px;
        }
        .tbody tr{
            border-top:1px solid var(--line);
        }
        .tbody td{
            padding:14px 16px; vertical-align:middle;
        }
        .id{ color:#6b7280; font-family:ui-monospace,SFMono-Regular,Menlo,monospace; }
        .name b{ font-weight:700; }
        .pill{
            display:inline-flex; align-items:center; gap:8px;
            background:var(--pill); border-radius:999px; padding:6px 12px; font-weight:600;
        }
        .dot{ width:10px; height:10px; border-radius:50%;}
        .dot.ok{ background:var(--ok);}
        .dot.warn{ background:var(--warn);}
        .dot.danger{ background:var(--danger);}
        .delete{
            display:inline-flex; align-items:center; justify-content:center;
            width:36px; height:36px; border-radius:10px; border:1px solid var(--line); background:#fff; cursor:pointer;
        }
        .delete:hover{ background:#fff3f3; border-color:#f7baba}
        .empty{ padding:40px; text-align:center; color:var(--muted); }
        .sticky-head{ position:sticky; top:0; z-index:1; }
        @media (max-width: 860px){
            .hide-sm{ display:none; }
        }
    </style>
</head>
<body>
<div class="wrap">

    <form class="toolbar" method="get" action="<c:url value='/facilities'/>">
        <div class="search">
            <input type="text" name="q" placeholder="검색: 시설명/도메인" value="${param.q}">
        </div>
        <button type="submit" class="btn">검색</button>
        <a class="btn" href="<c:url value='/facilities'/>">초기화</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>FACILITY_ID</th>
            <th>NAME</th>
            <th>DOMAIN</th>
            <th>UPDATED_AT</th>
            <th>STATUS</th>
            <th style="width:90px; text-align:center;">DELETE</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty facilities}">
                <c:forEach var="f" items="${facilities}">
                    <tr>
                        <td>${f.facilityId}</td>
                        <td>${f.name}</td>
                        <td>${f.domain}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty f.updatedAt}">
                                    <fmt:formatDate value="${f.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </c:when>
                                <c:otherwise>—</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${empty f.status ? '—' : f.status}</td>
                        <td style="text-align:center;">
                            <form method="post" action="<c:url value='/facilities/${f.facilityId}/delete'/>"
                                  onsubmit="return confirm('삭제하시겠습니까? (ID: ${f.facilityId})');">
                                <button type="submit" class="delete">삭제</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr><td class="empty" colspan="6">등록된 설비가 없습니다.</td></tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <c:if test="${not empty msg}">
        <p>${msg}</p>
    </c:if>
</div>
</body>
</html>
