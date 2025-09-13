<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" type="text/css"
              href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
        <script type="text/javascript"
                src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
        <script type="text/javascript"
                src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
        <script type="text/javascript"
                src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
        <link rel="stylesheet" href="<c:url value='/assets/css/reset.css'/>"/>
        <link rel="stylesheet" href="<c:url value='/assets/css/inspection.css?after'/>"/>
    </head>
    <body>
        <div class="container">
            <section class="card card-table">
                <div class="header">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                        <h3 style="font-size:23px;font-weight:600">점검 내역</h3>
                    </div>

                    <form method="get" action="<c:url value='/inspection/list'/>" class="filter-form">
                        <div class="filter-row">

                            <!-- 키워드 검색 -->
                            <input type="text" name="keyword" value="${param.keyword}" placeholder="설비명·점검자명 검색하기" class="input-search"/>

                            <!-- 날짜 범위 -->
                            <input type="text" name="dateRange"
                                   value="<c:if test='${not empty param.startDate and not empty param.endDate}'>${fn:trim(param.startDate)} ~ ${fn:trim(param.endDate)}</c:if>"
                           class="input-date"/>

                            <!-- hidden 으로 실제 검색 값 전달 -->
                            <input type="hidden" name="startDate" id="startDate" value="${param.startDate}"/>
                            <input type="hidden" name="endDate" id="endDate" value="${param.endDate}"/>

                            <!-- 상태 필터 -->
                            <select name="status" class="input-select">
                                <option value="">전체 상태</option>
                                <option value="PASS" <c:if test="${param.status == 'PASS'}">selected</c:if>>완료</option>
                                <option value="FAIL" <c:if test="${param.status == 'FAIL'}">selected</c:if>>이상 발견</option>
                            </select>

                            <!-- 검색 버튼 -->
                            <button type="submit" class="btn-search">검색</button>
                        </div>
                    </form>
                </div>

                <hr class="divider"/>

                <!-- 테이블 -->
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
                    <c:forEach var="r" items="${inspections}">
                        <tr>
                            <td>${r.domain}</td>
                            <td>
                                <c:out value="${r.facilityName}" />
                                <c:if test="${not empty r.floor}"> ${r.floor}층</c:if>
                                <c:if test="${not empty r.zone}"> ${r.zone}구역</c:if>
                            </td>
                            <td><fmt:formatDate value="${r.submittedAt}" pattern="yyyy년 M월 d일" /></td>
                            <td>${r.inspectorName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${r.result == 'PASS'}">
                                        <span class="badge ok">완료</span>
                                    </c:when>
                                    <c:when test="${r.result == 'FAIL'}">
                                        <span class="badge warn">이상 발견</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge">${r.result}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <!-- 페이지네이션 -->
                <div class="pagination">
                    <c:if test="${page > 1}">
                        <a href="?page=1&size=${param.size}&keyword=${param.keyword}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">&laquo;</a>
                        <a href="?page=${page-1}&size=${param.size}&keyword=${param.keyword}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">&lt;</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="p">
                        <a href="?page=${p}&size=${param.size}&keyword=${param.keyword}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}"
                           class="${p == page ? 'active' : ''}">
                                ${p}
                        </a>
                    </c:forEach>

                    <c:if test="${page < totalPages}">
                        <a href="?page=${page+1}&size=${param.size}&keyword=${param.keyword}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">&gt;</a>
                        <a href="?page=${totalPages}&size=${param.size}&keyword=${param.keyword}&startDate=${param.startDate}&endDate=${param.endDate}&status=${param.status}">&raquo;</a>
                    </c:if>
                </div>
            </section>
        </div>
        <script>
            $(document).ready(function () {
                $('input[name="dateRange"]').daterangepicker({
                    autoUpdateInput: false, // 직접 선택했을 때만 값 채우기
                    locale: {
                        format: 'YYYY-MM-DD',
                        separator: ' ~ ',
                        applyLabel: '적용',
                        cancelLabel: '취소',
                        monthNames: ["1월","2월","3월","4월","5월","6월",
                            "7월","8월","9월","10월","11월","12월"],
                        daysOfWeek: ["일","월","화","수","목","금","토"],
                        customRangeLabel: '사용자 지정'
                    },
                    ranges: {
                        '이번 달': [moment().startOf('month'), moment().endOf('month')],
                        '지난 달': [moment().subtract(1, 'month').startOf('month'),
                            moment().subtract(1, 'month').endOf('month')],
                        '최근 7일': [moment().subtract(6, 'days'), moment()],
                        '최근 30일': [moment().subtract(29, 'days'), moment()],
                        '최근 3개월': [moment().subtract(3, 'months').startOf('month'), moment()],
                        '최근 6개월': [moment().subtract(6, 'months').startOf('month'), moment()]
                    },
                    showDropdowns: true,
                    alwaysShowCalendars: true,
                    opens: "left",
                    linkedCalendars: false
                });

                // 적용 버튼 눌렀을 때 input 채우기 + hidden 값 설정
                $('input[name="dateRange"]').on('apply.daterangepicker', function(ev, picker) {
                    $(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
                    $('#startDate').val(picker.startDate.format('YYYY-MM-DD'));
                    $('#endDate').val(picker.endDate.format('YYYY-MM-DD'));
                });

                // 취소 버튼 눌렀을 때 초기화
                $('input[name="dateRange"]').on('cancel.daterangepicker', function(ev, picker) {
                    $(this).val('');
                    $('#startDate').val('');
                    $('#endDate').val('');
                });
            });
        </script>
    </body>
</html>