# Plataforma de Monitorização de Embalagens Inteligentes

## Descrição do Projeto

Este projeto visa desenvolver uma aplicação empresarial para monitorizar embalagens inteligentes equipadas com sensores e conectividade sem fio. A aplicação permitirá:
- Recolher dados de sensores que monitorizam temperatura, pressão atmosférica, aceleração e posicionamento global.
- Integrar e comunicar com outros sistemas empresariais, como logística, gestão operacional e apoio ao cliente.

O sistema será composto por um backend em **Jakarta EE** e um frontend em **Vue.js/Nuxt**, com persistência de dados usando **PostgreSQL**.

---

## Estrutura do Projeto

1. **Backend**:
   - Implementado como um serviço REST.
   - Inclui camadas de lógica de negócio e acesso a dados.
   - Base de dados relacional usando PostgreSQL.

2. **Frontend**:
   - Desenvolvido com Vue.js/Nuxt.
   - Simula o funcionamento de sistemas externos e sensores.
   - Consome os serviços REST do backend.

---

## Funcionalidades Principais

- Receber e processar mensagens de sensores ativos em embalagens.
- Associar dados de sensores a encomendas, volumes e produtos.
- Permitir consultas para gestores e clientes sobre o estado das encomendas.
- Integrar com sistemas de logística, gestão operacional e apoio ao cliente.

---

## Requisitos Tecnológicos

- **Backend**: Jakarta EE com REST Services.
- **Frontend**: Framework Vue.js/Nuxt.
- **Base de Dados**: PostgreSQL.
- **Arquitetura**: Seguir padrões como MVC, ORM e lazy loading.

---

## Participantes

- André Guerra - `https://github.com/AndreGuerra20` - `2221460@my.ipleiria.pt`
- Beatriz Cordeiro - `https://github.com/beatrizcordeiro04` - `2222398@my.ipleiria.pt`
- Daniela Dinis - `https://github.com/ddinis-pt` - `2221953@my.ipleiria.pt`
- Henrique Lemos - `https://github.com/HenriqueLemos1105` - `2222395@my.ipleiria.pt`

---

## Entregas

1. **Entrega Intermédia**:
   - Especificação da API REST.
2. **Entrega Final**:
   - Código completo (backend e frontend).
   - Especificação final da API REST (se modificada).
   - Apresentação e defesa do projeto.

---

## Como Executar

### Backend
1. Instalar Docker Desktop - https://www.docker.com/products/docker-desktop/
2. Instalar OpenJDK 17 (Temurin) - https://adoptium.net/temurin/releases/?version=17
3. Instalar o Chocolatey - https://chocolatey.org/install
4. Instalar as packages na raiz do projeto Java `pmei` com o comando `$ choco install maven make` 
5. Com o Docker Engine ligado (Engine running), na raiz do projeto Java `pmei` a correr executar o comando `$ make up`
6. Para parar o contentor do Docker ir a aplicação Docker Desktop e clicar no botão Stop


#### Opcional
- Instalar IntelliJ IDEA Ultimate - https://www.jetbrains.com/idea/download/?section=windows
- Para remover o contentor do docker pode fazer `$ make down` ou ir a aplicação Docker Desktop e fazer Delete diretamente 

### Frontend
1. Instalar Node.js - https://nodejs.org/pt
2. Instalar algum gerenciador de pacotes para o Node.js - https://www.npmjs.com/ - https://bun.sh/
3. Na raiz do projeto Vue `pmei-client` abrir uma consola e digitar o comando `$ npm install`
4. Para correr o projeto `$ npm run dev`

**Departamento de Engenharia Informática**  
**Desenvolvimento de Aplicações Empresariais - 2024/2025**
