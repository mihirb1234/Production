#!/bin/bash

# Simple API test script for Memes Commerce
# Usage: ./test-api.sh [base_url]

BASE_URL=${1:-"http://localhost:8080"}

echo "🧪 Testing Memes Commerce APIs"
echo "📍 Base URL: $BASE_URL"
echo "========================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test function
test_endpoint() {
    local method=$1
    local url=$2
    local expected_status=${3:-200}
    local description=$4

    echo -n "Testing $description... "

    if command -v curl &> /dev/null; then
        response=$(curl -s -w "%{http_code}" -X $method "$BASE_URL$url")
        status_code=$(echo "$response" | tail -c 3)
        body=$(echo "$response" | head -n -1)

        if [ "$status_code" -eq "$expected_status" ]; then
            echo -e "${GREEN}✅ PASS${NC} (Status: $status_code)"
        else
            echo -e "${RED}❌ FAIL${NC} (Expected: $expected_status, Got: $status_code)"
        fi
    else
        echo -e "${YELLOW}⚠️  SKIP${NC} (curl not found)"
    fi
}

echo "🔍 Testing Health Check..."
test_endpoint "GET" "/health_internal" 200 "Health Check"

echo ""
echo "👥 Testing User APIs..."
test_endpoint "GET" "/api/users" 200 "Get All Users"
test_endpoint "GET" "/api/users/1" 200 "Get User by ID"

echo ""
echo "📦 Testing Order APIs..."
test_endpoint "GET" "/api/orders/user/1" 200 "Get User Orders (Main API)"
test_endpoint "GET" "/api/orders/user/1/count" 200 "Get User Order Count"
test_endpoint "GET" "/api/orders/user/1/total-spent" 200 "Get User Total Spent"
test_endpoint "GET" "/api/orders/user/1/status/DELIVERED" 200 "Get User Orders by Status"

echo ""
echo "========================================"
echo "✨ API Testing Complete!"
echo ""
echo "💡 Pro Tips:"
echo "• Import 'MemesCommerce.postman_collection.json' into Postman for full testing"
echo "• Use Docker: ./docker-run.sh start"
echo "• Use MySQL Workbench for database queries"
echo "• Check logs: ./docker-run.sh logs"
