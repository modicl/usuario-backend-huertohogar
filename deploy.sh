#!/bin/bash

# Script de Despliegue en DigitalOcean Droplet
# Uso: bash deploy.sh

set -e

echo "🚀 Iniciando despliegue en DigitalOcean..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# ============================================
# 1. VALIDAR ARGUMENTOS
# ============================================
if [ -z "$1" ]; then
    echo -e "${RED}❌ Error: Debes proporcionar la IP del Droplet${NC}"
    echo "Uso: bash deploy.sh <IP_DROPLET> [rama]"
    echo "Ejemplo: bash deploy.sh 192.168.1.100 main"
    exit 1
fi

DROPLET_IP="$1"
BRANCH="${2:-main}"

echo -e "${YELLOW}📍 Droplet: $DROPLET_IP${NC}"
echo -e "${YELLOW}📍 Rama: $BRANCH${NC}"

# ============================================
# 2. CONECTARSE AL DROPLET
# ============================================
echo -e "${YELLOW}🔌 Conectando al Droplet...${NC}"

ssh -o StrictHostKeyChecking=no root@${DROPLET_IP} << 'EOF'

echo "🏗️  Construyendo imagen Docker..."

# Ir al directorio del proyecto
cd /root/usuario-backend-huertohogar 2>/dev/null || {
    echo "❌ Directorio no encontrado. Clonando repositorio..."
    cd /root
    git clone https://github.com/tu-usuario/usuario-backend-huertohogar.git
    cd usuario-backend-huertohogar
}

# Actualizar código
echo "📥 Actualizando código..."
git fetch origin
git checkout $BRANCH
git pull origin $BRANCH

# Crear archivo .env si no existe
if [ ! -f .env ]; then
    echo "⚙️  Creando archivo .env..."
    cp .env.example .env
    echo -e "${YELLOW}⚠️  Edita .env con las variables correctas${NC}"
    echo "⚠️  Usa: nano .env"
    exit 1
fi

# Iniciar servicios
echo "🐳 Iniciando servicios Docker..."
docker-compose down || true
docker-compose up -d

# Esperar a que la BD esté lista
echo "⏳ Esperando a que MySQL esté listo..."
sleep 10

# Verificar estado
echo "✅ Verificando servicios..."
docker-compose ps

# Health check
echo "🏥 Verificando health del backend..."
sleep 10
curl -f http://localhost:8080/actuator/health || {
    echo "⚠️  Health check fallo. Revisa logs:"
    docker-compose logs usuario-backend
}

echo -e "${GREEN}✅ Despliegue completado!${NC}"
echo ""
echo "📊 Ver logs en tiempo real:"
echo "  docker-compose logs -f usuario-backend"
echo ""
echo "🌐 Acceder a la aplicación:"
echo "  http://$DROPLET_IP:8080"

EOF

echo -e "${GREEN}✅ Script de despliegue ejecutado${NC}"
