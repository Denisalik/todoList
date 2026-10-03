#!/bin/bash
set -e

psql -U "user" -tc "SELECT 1 FROM pg_database WHERE datname = 'to_do'" | grep -q 1 || psql -U "user" -c "CREATE DATABASE to_do"