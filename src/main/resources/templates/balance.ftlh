<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bank Account</title>
</head>
<body>
<div>
    <form method="post" action="/balance/get_balance">
        <input type="text" name="amount" placeholder="Введите баланс счета">
        <button type="submit">Добавить</button>
    </form>
</div>
<div>Список сообщений</div>
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
        <#list balances as balance>
            <tr>
                <th>${balance.id}</th>
                <th>${balance.amount}</th>
                <th>
                    <form method="post" action="/balance/get_balance/${balance.id}">
                        <label>
                            <input type="text" name="amount" value="">
                        </label>
                        <button type="submit">Обновить</button>
                    </form>
                </th>
            </tr>
        <#else>
            No balances
        </#list>
    </table>
    <#if changeError??>
        <div style="color: red">${changeError}</div>
    </#if>
</#if>

</body>
</html>