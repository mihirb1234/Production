#!/bin/bash

set -e

PROJECT_NAME="memes-commercee"

case "$1" in
    build)
        echo "Building Docker images..."
        docker compose build --no-cache
        ;;
    start)
        echo "Starting services..."
        docker compose up -d
        echo "Services started. Application will be available at http://localhost:8080"
        ;;
    stop)
        echo "Stopping services..."
        docker compose down
        ;;
    restart)
        echo "Restarting services..."
        docker compose restart
        ;;
    logs)
        if [ -n "$2" ]; then
            docker compose logs -f "$2"
        else
            docker compose logs -f
        fi
        ;;
    clean)
        echo "Cleaning up Docker resources..."
        docker compose down -v
        docker system prune -f
        ;;
    status)
        docker compose ps
        ;;
    *)
        echo "Usage: $0 {build|start|stop|restart|logs|clean|status}"
        exit 1
esac
