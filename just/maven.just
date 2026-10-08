migrate:
    ./mvnw flyway:migrate
# maven build without tests
build:
    ./mvnw -DskipTests clean package
# execute linters
lint:
    ./mvnw checkstyle:check pmd:check

# start spring boot
spring:
    ./mvnw -DskipTests spring-boot:run

test:
    ./mvnw test

ci: lint test

# dump compile dependencies to dependencies.txt
dependencies:
  ./mvnw dependency:tree -Dscope=compile > dependencies.txt

# dump dependencies updates to updates.txt
updates:
  ./mvnw versions:display-dependency-updates > updates.txt