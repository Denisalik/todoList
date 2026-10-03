if [ -f src/main/resources/rsa/private.pem ] && [ -f src/main/resources/rsa/public.pem ]; then
  echo "RSA keys already exist, skipping generation."
else
  mkdir -p src/main/resources/rsa
  cd src/main/resources/rsa
  openssl genrsa -out private.pem 2048
  openssl rsa -in private.pem -pubout -out public.pem
fi