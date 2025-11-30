#!/bin/bash

# Docker run script for Memes Commerce application
# Usage: ./docker-run.sh [build|start|stop|restart|logs|clean]

set -e

PROJECT_NAME="memes-commercee"

case "$1" in
    build)
        echo "Building Docker images..."
        docker-compose build --no-cache
        ;;
    start)
        echo "Starting services..."
        docker-compose up -d
        echo "Services started. Application will be available at http://localhost:8080"
        ;;
    stop)
        echo "Stopping services..."
        docker-compose down
        ;;
    restart)
        echo "Restarting services..."
        docker-compose restart
        ;;
    logs)
        if [ -n "$2" ]; then
            docker-compose logs -f "$2"
        else
            docker-compose logs -f
        fi
        ;;
    clean)
        echo "Cleaning up Docker resources..."
        docker-compose down -v
        docker system prune -f
        ;;
    status)
        docker-compose ps
        ;;
    *)
        echo "Usage: $0 {build|start|stop|restart|logs|clean|status}"
        echo ""
        echo "Commands:"
        echo "  build    - Build Docker images"
        echo "  start    - Start all services"
        echo "  stop     - Stop all services"
        echo "  restart  - Restart all services"
        echo "  logs     - Show logs (optionally specify service name)"
        echo "  clean    - Remove containers, volumes, and prune system"
        echo "  status   - Show status of services"
        exit 1
esac
