#!/usr/bin/env bash

echo "Content-type: text/html"
echo ""
java WebApp "${QUERY_STRING:-no:args}" 2>&1
