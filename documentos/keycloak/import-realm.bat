@echo off
setlocal EnableDelayedExpansion

echo Keycloak iniciado. Solicitando token do admin...

:: Solicita token de admin e salva no arquivo
curl -s ^
  -d "client_id=admin-cli" ^
  -d "username=admin" ^
  -d "password=admin" ^
  -d "grant_type=password" ^
  http://localhost:8080/realms/master/protocol/openid-connect/token > token.json

:: Extrai o token de acesso usando PowerShell
for /f "delims=" %%i in ('powershell -Command "(Get-Content token.json | ConvertFrom-Json).access_token"') do set TOKEN=%%i

:: Remove o arquivo temporário
del token.json

echo Importando o realm extrato...

curl -s -X POST http://localhost:8080/admin/realms ^
  -H "Authorization: Bearer %TOKEN%" ^
  -H "Content-Type: application/json" ^
  -d @realm-extrato.json > nul

echo Realm 'extrato' importado com sucesso!
