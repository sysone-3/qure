<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <h2>오늘 점검</h2>
    <p>이름: ${vm.name}</p>
    <p>이메일: ${vm.email}</p>
    <p>전체 개수: ${todayStats.todayTotal}</p>
    <p>완료 개수: ${todayStats.todayCompleted}</p>

    <h2>기타 지표</h2>
    <p>어제까지 전체 개수: ${otherStats.untilYesterdayTotal}</p>
    <p>어제까지 완료 개수: ${otherStats.untilYesterdayCompleted}</p>
    <p>예정 점검 수: ${otherStats.upcomingCount}</p>
    <p>미점검 현황: ${otherStats.overdueUninspected}</p>
    <p>처리되지 않은 민원: ${otherStats.openComplaints}</p>
</head>
<body>

</body>
</html>
