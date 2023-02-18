<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bank Account</title>
</head>
<body>
<table>
    <tr>
        <th>writeCount</th>
        <th>readCount</th>
        <th>allReadCount</th>
    </tr>
    <tr>
        <th>${writeCount}</th>
        <th>${readCount}</th>
        <th>${allReadCount}</th>
    </tr>
</table>
<div>Сообщение</div>
<form method="get" action="/balance/get_balance">
    <label>
        <#if id??>
            <input type="text" name="id" value="${id}">
        <#else>
            <input type="text" name="id" value="">
        </#if>
    </label>
    <button type="submit">Найти</button>
</form>
<#if getError??>
    <div style="color: red">${getError}</div>
<#else>
    <table>
        <tr>
            <th>ID</th>
            <th>Значение</th>
            <th>Изменить Значение</th>
        </tr>
        <#if balance??>
            <tr>
                <th>${id}</th>
                <th>${balance}</th>
                <th>
                    <form method="post" action="/balance/get_balance/${id}">
                        <label>
                            <input type="text" name="amount" value="">
                        </label>
                        <button type="submit">Обновить</button>
                    </form>
                </th>
            </tr>
        <#else>
            No balances
        </#if>
    </table>
    <#if changeError??>
        <div style="color: red">${changeError}</div>
    </#if>
</#if>

</body>
</html>