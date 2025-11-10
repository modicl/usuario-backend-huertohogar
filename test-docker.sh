#!/bin/bash

# Script para Pruebas Locales con Docker
# Uso: bash test-docker.sh [comando]

set -e

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# ============================================
# FUNCIONES
# ============================================

print_header() {
    echo -e "${BLUE}▶ $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# ============================================
# COMANDOS DISPONIBLES
# ============================================

cmd_start() {
    print_header "Iniciando servicios Docker..."
    
    if [ ! -f .env ]; then
        print_warning "Archivo .env no encontrado. Creando desde .env.example..."
        cp .env.example .env
    fi
    
    docker-compose up -d
    print_success "Servicios iniciados"
    
    echo ""
    print_header "Esperando a que MySQL esté listo (10 segundos)..."
    sleep 10
    
    echo ""
    print_header "Estado de servicios:"
    docker-compose ps
    
    echo ""
    print_success "Para ver logs: docker-compose logs -f"
}

cmd_stop() {
    print_header "Deteniendo servicios Docker..."
    docker-compose down
    print_success "Servicios detenidos"
}

cmd_restart() {
    print_header "Reiniciando servicios Docker..."
    docker-compose restart
    print_success "Servicios reiniciados"
}

cmd_logs() {
    print_header "Mostrando logs en tiempo real..."
    docker-compose logs -f usuario-backend
}

cmd_logs_db() {
    print_header "Mostrando logs de MySQL..."
    docker-compose logs -f mysql
}

cmd_build() {
    print_header "Construyendo imagen Docker..."
    docker build -t usuario-backend:local .
    print_success "Imagen construida: usuario-backend:local"
}

cmd_health() {
    print_header "Verificando health check..."
    
    if ! docker-compose ps | grep -q "usuario-backend.*Up"; then
        print_error "Servicio no está corriendo. Usa: bash test-docker.sh start"
        return 1
    fi
    
    echo ""
    print_header "Conectando a http://localhost:8080/actuator/health"
    
    if curl -s http://localhost:8080/actuator/health | jq . 2>/dev/null; then
        print_success "Health check: OK"
    else
        print_error "Health check fallo"
        return 1
    fi
}

cmd_test_endpoints() {
    print_header "Probando endpoints principales..."
    
    BASE_URL="http://localhost:8080"
    
    # Endpoint público (no requiere token)
    print_header "1. POST /authenticate (público)..."
    curl -X POST "$BASE_URL/authenticate" \
        -H "Content-Type: application/json" \
        -d '{
            "nombreUsuario": "test",
            "contrasena": "password123"
        }' \
        -w "\nHTTP Status: %{http_code}\n\n" || print_warning "Endpoint no disponible"
    
    # Endpoint protegido (sin token debe fallar)
    print_header "2. GET /usuarios/1 (sin token - debe fallar)..."
    curl -X GET "$BASE_URL/usuarios/1" \
        -w "\nHTTP Status: %{http_code}\n\n" || print_warning "Endpoint no disponible"
    
    print_warning "Nota: Para endpoints protegidos necesitas un JWT token válido"
}

cmd_shell() {
    print_header "Abriendo shell en contenedor usuario-backend..."
    docker exec -it usuario-backend /bin/sh
}

cmd_shell_db() {
    print_header "Abriendo MySQL CLI..."
    docker exec -it usuario-backend-db mysql -u root -proot_password -e "USE huerto_hogar; SHOW TABLES;"
}

cmd_clean() {
    print_header "Limpiando contenedores y volúmenes..."
    
    print_warning "Esto eliminará todos los datos de la BD local"
    read -p "¿Estás seguro? (s/n): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Ss]$ ]]; then
        docker-compose down -v
        print_success "Limpieza completada"
    else
        print_warning "Limpieza cancelada"
    fi
}

cmd_help() {
    cat << EOF

${BLUE}╔════════════════════════════════════════════════════════════╗${NC}
${BLUE}║     Script de Pruebas Locales - Usuario Backend Docker     ║${NC}
${BLUE}╚════════════════════════════════════════════════════════════╝${NC}

${GREEN}COMANDOS DISPONIBLES:${NC}

  ${YELLOW}bash test-docker.sh start${NC}
    Inicia los servicios Docker (MySQL + Backend)

  ${YELLOW}bash test-docker.sh stop${NC}
    Detiene los servicios Docker

  ${YELLOW}bash test-docker.sh restart${NC}
    Reinicia los servicios

  ${YELLOW}bash test-docker.sh logs${NC}
    Muestra logs del backend en tiempo real

  ${YELLOW}bash test-docker.sh logs-db${NC}
    Muestra logs de MySQL en tiempo real

  ${YELLOW}bash test-docker.sh build${NC}
    Construye la imagen Docker

  ${YELLOW}bash test-docker.sh health${NC}
    Verifica el estado de salud del backend

  ${YELLOW}bash test-docker.sh test-endpoints${NC}
    Prueba los endpoints principales

  ${YELLOW}bash test-docker.sh shell${NC}
    Abre una shell en el contenedor del backend

  ${YELLOW}bash test-docker.sh shell-db${NC}
    Abre MySQL CLI en el contenedor de BD

  ${YELLOW}bash test-docker.sh clean${NC}
    Elimina contenedores y volúmenes (PELIGROSO)

  ${YELLOW}bash test-docker.sh help${NC}
    Muestra esta ayuda

${BLUE}╔════════════════════════════════════════════════════════════╗${NC}
${BLUE}║                    EJEMPLOS DE USO                         ║${NC}
${BLUE}╚════════════════════════════════════════════════════════════╝${NC}

  # Iniciar servicios y ver logs
  bash test-docker.sh start
  bash test-docker.sh logs

  # Verificar que todo está funcionando
  bash test-docker.sh health

  # Probar endpoints
  bash test-docker.sh test-endpoints

  # Limpieza completa
  bash test-docker.sh clean
  bash test-docker.sh start

${BLUE}╔════════════════════════════════════════════════════════════╗${NC}
${BLUE}║                  ACCESO A SERVICIOS                        ║${NC}
${BLUE}╚════════════════════════════════════════════════════════════╝${NC}

  🌐 Backend:  http://localhost:8080
  📊 Health:   http://localhost:8080/actuator/health
  🗄️  MySQL:    localhost:3306 (usuario_backend / usuario_backend_pass)

${BLUE}╔════════════════════════════════════════════════════════════╗${NC}
${BLUE}║              VARIABLES DE ENTORNO                          ║${NC}
${BLUE}╚════════════════════════════════════════════════════════════╝${NC}

  Editar: nano .env

  Variables principales:
  - SPRING_DATASOURCE_URL
  - SPRING_DATASOURCE_USERNAME
  - SPRING_DATASOURCE_PASSWORD
  - JWT_SECRET
  - JAVA_OPTS

EOF
}

# ============================================
# MAIN
# ============================================

COMMAND="${1:-help}"

case "$COMMAND" in
    start)
        cmd_start
        ;;
    stop)
        cmd_stop
        ;;
    restart)
        cmd_restart
        ;;
    logs)
        cmd_logs
        ;;
    logs-db)
        cmd_logs_db
        ;;
    build)
        cmd_build
        ;;
    health)
        cmd_health
        ;;
    test-endpoints)
        cmd_test_endpoints
        ;;
    shell)
        cmd_shell
        ;;
    shell-db)
        cmd_shell_db
        ;;
    clean)
        cmd_clean
        ;;
    help|--help|-h)
        cmd_help
        ;;
    *)
        print_error "Comando no reconocido: $COMMAND"
        echo "Usa: bash test-docker.sh help"
        exit 1
        ;;
esac
