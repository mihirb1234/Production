@echo off
setlocal enabledelayedexpansion

REM Docker Run Script for Memes Commerce Application (Windows)
REM Usage: docker-run.bat [build|start|stop|restart|logs|clean|status]

set PROJECT_NAME=memes-commercee

if "%1"=="" (
    goto :usage
)

if "%1"=="build" goto :build
if "%1"=="start" goto :start
if "%1"=="stop" goto :stop
if "%1"=="restart" goto :restart
if "%1"=="logs" goto :logs
if "%1"=="clean" goto :clean
if "%1"=="status" goto :status
goto :usage

:build
echo Building Docker images...
docker-compose build --no-cache
goto :end

:start
echo Starting services...
docker-compose up -d
echo.
echo Services started. Application will be available at http://localhost:8080
echo.
echo To view logs: docker-run.bat logs
echo To check status: docker-run.bat status
goto :end

:stop
echo Stopping services...
docker-compose down
goto :end

:restart
echo Restarting services...
docker-compose restart
goto :end

:logs
if "%2"=="" (
    docker-compose logs -f
) else (
    docker-compose logs -f %2
)
goto :end

:clean
echo Cleaning up Docker resources...
docker-compose down -v
docker system prune -f
goto :end

:status
docker-compose ps
goto :end

:usage
echo Usage: docker-run.bat {build^|start^|stop^|restart^|logs^|clean^|status}
echo.
echo Commands:
echo   build    - Build Docker images
echo   start    - Start all services
echo   stop     - Stop all services
echo   restart  - Restart all services
echo   logs     - Show logs ^(optionally specify service name^)
echo   clean    - Remove containers, volumes, and prune system
echo   status   - Show status of services
echo.
echo Examples:
echo   docker-run.bat start
echo   docker-run.bat logs app
echo   docker-run.bat clean
goto :end

:end
