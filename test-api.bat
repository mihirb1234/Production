@echo off
REM Simple API test script for Memes Commerce (Windows)
REM Usage: test-api.bat [base_url]

setlocal enabledelayedexpansion

if "%1"=="" (
    set BASE_URL=http://localhost:8080
) else (
    set BASE_URL=%1
)

echo ========================================
echo 🧪 Testing Memes Commerce APIs
echo 📍 Base URL: !BASE_URL!
echo ========================================
echo.

REM Colors for Windows CMD (limited support)
set GREEN=[OK]
set RED=[FAIL]
set YELLOW=[SKIP]

echo 🔍 Testing Health Check...
call :test_endpoint "GET" "/health_internal" 200 "Health Check"

echo.
echo 👥 Testing User APIs...
call :test_endpoint "GET" "/api/users" 200 "Get All Users"
call :test_endpoint "GET" "/api/users/1" 200 "Get User by ID"

echo.
echo 📦 Testing Order APIs...
call :test_endpoint "GET" "/api/orders/user/1" 200 "Get User Orders (Main API)"
call :test_endpoint "GET" "/api/orders/user/1/count" 200 "Get User Order Count"
call :test_endpoint "GET" "/api/orders/user/1/total-spent" 200 "Get User Total Spent"
call :test_endpoint "GET" "/api/orders/user/1/status/DELIVERED" 200 "Get User Orders by Status"

echo.
echo ========================================
echo ✨ API Testing Complete!
echo.
echo 💡 Pro Tips:
echo • Import 'MemesCommerce.postman_collection.json' into Postman for full testing
echo • Use Docker: docker-run.bat start
echo • Use MySQL Workbench for database queries
echo • Check logs: docker-run.bat logs
goto :end

:test_endpoint
set method=%1
set url=%2
set expected_status=%3
set description=%4

echo Testing !description!...

REM Check if curl is available
curl --version >nul 2>&1
if errorlevel 1 (
    echo !YELLOW! SKIP (curl not found)
    goto :eof
)

REM Make the request and capture both response and status
for /f "tokens=*" %%i in ('curl -s -w "%%{http_code}" -X %method% "!BASE_URL!%url%"') do set response=%%i
set status_code=!response:~-3!

if "!status_code!"=="%expected_status%" (
    echo !GREEN! PASS (Status: !status_code!)
) else (
    echo !RED! FAIL (Expected: %expected_status%, Got: !status_code!)
)

goto :eof

:end
