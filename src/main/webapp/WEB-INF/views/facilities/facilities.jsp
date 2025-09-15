<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설비 목록</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 설비 페이지 전용 CSS -->
    <!-- 기본 레이아웃 CSS 추가 -->
    <link rel="stylesheet" href="<c:url value='/assets/css/navigation.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/components.css'/>" />
    <link rel="stylesheet" href="<c:url value='/assets/css/facilities.css'/>" />
</head>
<body>
<!-- nav 변수 설정 (사이드바에서 '설비' 메뉴 활성화) -->
<c:set var="nav" value="facilities" scope="request"/>

<div class="layout">
    <!-- 사이드바 포함 -->
    <%@ include file="../fragments/sidebar.jspf" %>

    <!-- 메인 콘텐츠 -->
    <main class="content">
        <div class="page">
            <!-- 검색/필터 -->
            <div class="card">
                <form class="toolbar" method="get" action="<c:url value='/facilities'/>">
                    <h3 style="font-size:23px;font-weight:600">설비 목록</h3>
                    <div style="display: flex; align-items: center; justify-content: space-between; flex: 0.5">
                        <div class="search">
                            <!-- 돋보기 -->
                            <svg class="glass" viewBox="0 0 24 24" fill="none">
                                <path d="M21 21l-4.3-4.3M10.5 18a7.5 7.5 0 1 1 0-15 7.5 7.5 0 0 1 0 15Z" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/>
                            </svg>
                            <input type="text" name="q" placeholder="검색: 시설명/도메인" value="${param.q}">
                        </div>
                        <button type="submit" class="btn-search">검색</button>
                        <!-- 상단 우측 설비 추가 -->
                        <div class="top-actions">
                            <a class="btn btn-brand btn-add" href="<c:url value='/facilities/new'/>">설비 추가</a>
                        </div>
                    </div>
                </form>

                <!-- 헤더 -->
                <div class="list-head">
                    <div class="row head">
                        <div class="cell col-id">설비 id</div>
                        <div class="cell col-facility">시설명</div>
                        <div class="cell col-domain">도메인</div>
                        <div class="cell col-equip">설비명</div>
                        <div class="cell col-updated">최근 점검일자</div>
                        <div class="cell col-actions">점검 예정일자</div>
                    </div>
                </div>

                <!-- 바디 -->
                <div class="list">
                    <c:if test="${not empty msg}">
                        <p style="margin:16px;color:#6b7280">${msg}</p>
                    </c:if>
                    <c:choose>
                        <c:when test="${not empty facilities}">
                            <c:forEach var="f" items="${facilities}">
                                <a href="<c:url value='/facilities/${f.facilityId}'/>">
                                    <div class="row">
                                        <!-- 설비 id -->
                                        <div class="cell col-id">${f.facilityId}</div>

                                        <!-- 시설명 -->
                                        <div class="cell col-facility">${f.name}</div>

                                        <!-- 도메인 -->
                                        <div class="cell col-domain">${f.domain}</div>

                                        <!-- 설비명  -->
                                        <div class="cell col-equip">${f.zone} </div>

                                        <!-- 최근 점검일자 -->
                                        <div class="cell col-updated">
                                            <c:choose>
                                                <c:when test="${not empty f.updatedAt}">
                                                    <fmt:formatDate value="${f.updatedAt}" pattern="yyyy.MM.dd"/>
                                                </c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </div>

                                        <!-- 다음 점검 -->
                                        <div class="cell col-actions">
                                            <div class="facility-header">
                                                <c:choose>
                                                    <c:when test="${f.scheduleStatus == 'OVERDUE'}">
                                                        <span class="badge badge-danger">점검 지연</span>
                                                        <span class="note">
                                                            <fmt:formatDate value="${f.nextScheduledAt}" pattern="yyyy.MM.dd"/>
                                                            &nbsp;이후 <strong>${f.daysDelta}</strong>일 경과
                                                          </span>
                                                    </c:when>

                                                    <c:when test="${f.scheduleStatus == 'UPCOMING'}">
                                                        <span class="badge badge-info">다음 점검</span>
                                                        <span class="note">
                                                            <fmt:formatDate value="${f.nextScheduledAt}" pattern="yyyy.MM.dd"/>
                                                            &nbsp;(D<c:if test="${f.daysDelta >= 0}">-</c:if>${f.daysDelta})
                                                          </span>
                                                    </c:when>

                                                    <c:otherwise>
                                                        <span class="badge">점검 예정 없음</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty">등록된 설비가 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <!-- 페이지네이션 UI 추가 -->
                <c:if test="${totalPages > 1}">
                    <nav class="pagination">
                        <!-- 처음 / 이전 -->
                        <a class="page-link ${page == 1 ? 'disabled' : ''}"
                           href="<c:url value='/facilities'>
                                    <c:param name='page' value='1'/>
                                    <c:if test='${not empty param.q}'>
                                        <c:param name='q' value='${param.q}'/>
                                    </c:if>
                                 </c:url>">« 처음</a>

                        <a class="page-link ${page == 1 ? 'disabled' : ''}"
                           href="<c:url value='/facilities'>
                                    <c:param name='page' value='${page-1}'/>
                                    <c:if test='${not empty param.q}'>
                                        <c:param name='q' value='${param.q}'/>
                                    </c:if>
                                 </c:url>">‹ 이전</a>

                        <!-- 가운데 숫자 -->
                        <c:set var="start" value="${page - 2 < 1 ? 1 : page - 2}"/>
                        <c:set var="end" value="${start + 4}"/>
                        <c:if test="${end > totalPages}">
                            <c:set var="end" value="${totalPages}"/>
                            <c:set var="start" value="${end - 4 < 1 ? 1 : end - 4}"/>
                        </c:if>

                        <c:forEach var="p" begin="${start}" end="${end}">
                            <a class="page-link ${p == page ? 'active' : ''}"
                               href="<c:url value='/facilities'>
                                        <c:param name='page' value='${p}'/>
                                        <c:if test='${not empty param.q}'>
                                            <c:param name='q' value='${param.q}'/>
                                        </c:if>
                                     </c:url>">${p}</a>
                        </c:forEach>

                        <!-- 다음 / 마지막 -->
                        <a class="page-link ${page == totalPages ? 'disabled' : ''}"
                           href="<c:url value='/facilities'>
                                    <c:param name='page' value='${page+1}'/>
                                    <c:if test='${not empty param.q}'>
                                        <c:param name='q' value='${param.q}'/>
                                    </c:if>
                                 </c:url>">다음 ›</a>

                        <a class="page-link ${page == totalPages ? 'disabled' : ''}"
                           href="<c:url value='/facilities'>
                                    <c:param name='page' value='${totalPages}'/>
                                    <c:if test='${not empty param.q}'>
                                        <c:param name='q' value='${param.q}'/>
                                    </c:if>
                                 </c:url>">마지막 »</a>
                    </nav>
                </c:if>
            </div>
        </div>
    </main>
</div>
</body>
</html>