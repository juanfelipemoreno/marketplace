
const Views = Views || {};

Views.home = {
    render() {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="animate-fade-in">
                <!-- Hero Section -->
                <div class="bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-lg shadow-xl p-12 mb-8">
                    <h1 class="text-5xl font-bold mb-4">Bienvenido a Marketplace</h1>
                    <p class="text-xl mb-6">El mejor lugar para comprar y vender productos físicos y digitales</p>
                    <div class="space-x-4">
                        <a href="#catalogo" class="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 inline-block">
                            Explorar Catálogo
                        </a>
                        ${!App.currentUser ? `
                            <a href="#registro" class="bg-blue-800 text-white px-8 py-3 rounded-lg font-semibold hover:bg-blue-900 inline-block">
                                Registrarse Gratis
                            </a>
                        ` : ''}
                    </div>
                </div>

                <!-- Características -->
                <div class="grid md:grid-cols-3 gap-6 mb-8">
                    <div class="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition">
                        <i class="fas fa-box text-blue-600 text-4xl mb-4"></i>
                        <h3 class="text-xl font-bold mb-2">Productos Físicos</h3>
                        <p class="text-gray-600">Encuentra ropa, accesorios, dispositivos electrónicos y más</p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition">
                        <i class="fas fa-cloud-download-alt text-purple-600 text-4xl mb-4"></i>
                        <h3 class="text-xl font-bold mb-2">Productos Digitales</h3>
                        <p class="text-gray-600">E-books, software, cursos en línea y música digital</p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition">
                        <i class="fas fa-shield-alt text-green-600 text-4xl mb-4"></i>
                        <h3 class="text-xl font-bold mb-2">Compra Segura</h3>
                        <p class="text-gray-600">Sistema de pagos seguro y protección al comprador</p>
                    </div>
                </div>

                <!-- Estadísticas -->
                ${App.currentUser ? `
                    <div class="bg-white p-8 rounded-lg shadow-lg">
                        <h2 class="text-2xl font-bold mb-6">Tu Actividad</h2>
                        <div class="grid md:grid-cols-4 gap-4" id="userStats">
                            <div class="text-center p-4 bg-blue-50 rounded-lg">
                                <i class="fas fa-shopping-cart text-blue-600 text-3xl mb-2"></i>
                                <p class="text-2xl font-bold" id="statCarrito">0</p>
                                <p class="text-gray-600 text-sm">En Carrito</p>
                            </div>
                            <div class="text-center p-4 bg-green-50 rounded-lg">
                                <i class="fas fa-receipt text-green-600 text-3xl mb-2"></i>
                                <p class="text-2xl font-bold" id="statOrdenes">0</p>
                                <p class="text-gray-600 text-sm">Órdenes</p>
                            </div>
                            ${App.currentUser.rol === 'VENDEDOR' ? `
                                <div class="text-center p-4 bg-purple-50 rounded-lg">
                                    <i class="fas fa-box text-purple-600 text-3xl mb-2"></i>
                                    <p class="text-2xl font-bold" id="statProductos">0</p>
                                    <p class="text-gray-600 text-sm">Mis Productos</p>
                                </div>
                                <div class="text-center p-4 bg-orange-50 rounded-lg">
                                    <i class="fas fa-chart-line text-orange-600 text-3xl mb-2"></i>
                                    <p class="text-2xl font-bold" id="statVentas">0</p>
                                    <p class="text-gray-600 text-sm">Ventas</p>
                                </div>
                            ` : `
                                <div class="text-center p-4 bg-purple-50 rounded-lg">
                                    <i class="fas fa-download text-purple-600 text-3xl mb-2"></i>
                                    <p class="text-2xl font-bold" id="statDescargas">0</p>
                                    <p class="text-gray-600 text-sm">Descargas</p>
                                </div>
                            `}
                        </div>
                    </div>
                ` : ''}
            </div>
        `;

        if (App.currentUser) {
            this.loadUserStats();
        }
    },

    async loadUserStats() {
        try {
            if (App.cart) {
                document.getElementById('statCarrito').textContent = App.cart.totalItems || 0;
            }


            const ordenesResult = await API.ordenes.listarMisOrdenes();
            document.getElementById('statOrdenes').textContent = ordenesResult.data?.length || 0;

            if (App.currentUser.rol === 'VENDEDOR') {
                const productosResult = await API.productos.listarMisProductos();
                document.getElementById('statProductos').textContent = productosResult.data?.length || 0;

                const ventasResult = await API.ordenes.listarVentas();
                document.getElementById('statVentas').textContent = ventasResult.data?.length || 0;
            } else if (App.currentUser.rol === 'COMPRADOR') {
                const descargasResult = await API.descargas.listar();
                document.getElementById('statDescargas').textContent = descargasResult.data?.length || 0;
            }
        } catch (error) {
            console.error('Error al cargar estadísticas:', error);
        }
    }
};