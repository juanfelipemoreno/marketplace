
const App = {
    currentUser: null,
    currentPage: 'home',
    cart: null,

    async init() {
        await this.checkSession();
        this.loadInitialPage();
        this.setupEventListeners();
    },

    async checkSession() {
        try {
            const result = await API.usuarios.obtenerPerfil();
            this.currentUser = result.data;
            this.updateNavbar();
        } catch (error) {
            console.log('No hay sesión activa');
        }
    },

    loadInitialPage() {
        const hash = window.location.hash.slice(1) || CONFIG.DEFAULT_PAGE;
        this.navigateTo(hash);
    },

    setupEventListeners() {
        window.addEventListener('hashchange', () => {
            const page = window.location.hash.slice(1) || CONFIG.DEFAULT_PAGE;
            this.navigateTo(page);
        });
    },

    navigateTo(page) {
        this.currentPage = page;
        window.location.hash = page;
        this.renderPage(page);
    },

    updateNavbar() {
        const navMenu = document.getElementById('navMenu');

        if (this.currentUser) {
            navMenu.innerHTML = `
                <a href="#home" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                    <i class="fas fa-home mr-1"></i> Inicio
                </a>
                <a href="#catalogo" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                    <i class="fas fa-shopping-bag mr-1"></i> Catálogo
                </a>
                ${this.currentUser.rol === 'VENDEDOR' || this.currentUser.rol === 'ADMIN' ? `
                    <a href="#mis-productos" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                        <i class="fas fa-box mr-1"></i> Mis Productos
                    </a>
                ` : ''}
                ${this.currentUser.rol === 'ADMIN' ? `
                    <a href="#admin" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                        <i class="fas fa-cog mr-1"></i> Admin
                    </a>
                ` : ''}
                <a href="#carrito" class="text-gray-700 hover:text-blue-600 px-3 py-2 relative">
                    <i class="fas fa-shopping-cart mr-1"></i> Carrito
                    <span id="cartCount" class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">0</span>
                </a>
                <a href="#ordenes" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                    <i class="fas fa-receipt mr-1"></i> Órdenes
                </a>
                ${this.currentUser.rol === 'COMPRADOR' ? `
                    <a href="#descargas" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                        <i class="fas fa-download mr-1"></i> Descargas
                    </a>
                ` : ''}
                <a href="#perfil" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                    <i class="fas fa-user mr-1"></i> ${this.currentUser.nombre}
                </a>
                <button onclick="App.logout()" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                    <i class="fas fa-sign-out-alt mr-1"></i> Salir
                </button>
            `;
            this.loadCart();
        } else {
            navMenu.innerHTML = `
                <a href="#home" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                    <i class="fas fa-home mr-1"></i> Inicio
                </a>
                <a href="#catalogo" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                    <i class="fas fa-shopping-bag mr-1"></i> Catálogo
                </a>
                <a href="#login" class="text-gray-700 hover:text-blue-600 px-3 py-2">
                    <i class="fas fa-sign-in-alt mr-1"></i> Iniciar Sesión
                </a>
                <a href="#registro" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                    <i class="fas fa-user-plus mr-1"></i> Registrarse
                </a>
            `;
        }
    },

    async loadCart() {
        try {
            const result = await API.carrito.obtener();
            this.cart = result.data;
            this.updateCartCount();
        } catch (error) {
            console.error('Error al cargar carrito:', error);
        }
    },

    updateCartCount() {
        const cartCount = document.getElementById('cartCount');
        if (cartCount && this.cart) {
            cartCount.textContent = this.cart.totalItems || 0;
        }
    },

    async logout() {
        try {
            await API.usuarios.logout();
            this.currentUser = null;
            this.cart = null;
            this.updateNavbar();
            this.navigateTo('home');
            showToast('Sesión cerrada exitosamente', 'success');
        } catch (error) {
            showToast('Error al cerrar sesión', 'error');
        }
    },

    renderPage(page) {
        showLoader();

        setTimeout(() => {
            switch (page) {
                case 'home':
                    Views.home.render();
                    break;
                case 'login':
                    Views.auth.renderLogin();
                    break;
                case 'registro':
                    Views.auth.renderRegistro();
                    break;
                case 'catalogo':
                    Views.catalogo.render();
                    break;
                case 'carrito':
                    Views.carrito.render();
                    break;
                case 'ordenes':
                    Views.ordenes.render();
                    break;
                case 'perfil':
                    Views.perfil.render();
                    break;
                case 'mis-productos':
                    Views.productos.render();
                    break;
                case 'admin':
                    Views.admin.render();
                    break;
                case 'descargas':
                    Views.descargas.render();
                    break;
                default:
                    Views.home.render();
            }
        }, 300);
    }
};