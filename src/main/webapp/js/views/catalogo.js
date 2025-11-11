
Views.catalogo = {
    productos: [],
    categorias: [],
    filtroActual: null,

    async render() {
        const content = document.getElementById('mainContent');
        content.innerHTML = '<div class="flex justify-center py-20"><div class="loader"></div></div>';

        try {
 
            const [productosResult, categoriasResult] = await Promise.all([
                API.catalogo.listar(),
                API.categorias.listarActivas()
            ]);

            this.productos = productosResult.data || [];
            this.categorias = categoriasResult.data || [];

            this.renderContent();
        } catch (error) {
            content.innerHTML = `
                <div class="text-center py-20">
                    <i class="fas fa-exclamation-circle text-6xl text-red-500 mb-4"></i>
                    <p class="text-red-500 text-xl">Error al cargar el catálogo</p>
                    <button onclick="Views.catalogo.render()" class="mt-4 bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700">
                        Reintentar
                    </button>
                </div>
            `;
        }
    },

    renderContent() {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="animate-fade-in">
                <h2 class="text-3xl font-bold mb-6">Catálogo de Productos</h2>
                
                <!-- Barra de búsqueda y filtros -->
                <div class="mb-6 space-y-4">
                    <div class="flex gap-4">
                        <div class="flex-1">
                            <input type="text" id="searchInput" placeholder="Buscar productos..."
                                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                        </div>
                        <select id="categoriaFilter" class="px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
                            <option value="">Todas las categorías</option>
                            ${this.categorias.map(c => `<option value="${c.id}">${c.nombre}</option>`).join('')}
                        </select>
                        <select id="tipoFilter" class="px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
                            <option value="">Todos los tipos</option>
                            <option value="FISICO">Físico</option>
                            <option value="DIGITAL">Digital</option>
                        </select>
                    </div>
                </div>

                <!-- Productos -->
                <div id="productosGrid" class="grid md:grid-cols-3 lg:grid-cols-4 gap-6">
                    ${this.renderProductos(this.productos)}
                </div>
            </div>
        `;

        this.setupFilters();
    },

    renderProductos(productos) {
        if (productos.length === 0) {
            return `
                <div class="col-span-full text-center py-20">
                    <i class="fas fa-search text-6xl text-gray-300 mb-4"></i>
                    <p class="text-gray-600 text-xl">No se encontraron productos</p>
                </div>
            `;
        }

        return productos.map(p => `
            <div class="bg-white rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition">
                <img src="${p.imagenUrl || CONFIG.DEFAULT_IMAGE}" 
                    alt="${p.nombre}" 
                    class="w-full h-48 object-cover cursor-pointer"
                    onclick="Views.catalogo.verDetalle(${p.id})">
                <div class="p-4">
                    <span class="text-xs ${p.tipoProducto === 'DIGITAL' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'} px-2 py-1 rounded">
                        <i class="fas fa-${p.tipoProducto === 'DIGITAL' ? 'cloud-download-alt' : 'box'}"></i>
                        ${p.tipoProducto}
                    </span>
                    <h3 class="font-bold text-lg mt-2 line-clamp-2 cursor-pointer hover:text-blue-600"
                        onclick="Views.catalogo.verDetalle(${p.id})">${p.nombre}</h3>
                    <p class="text-gray-600 text-sm mt-1 line-clamp-2">${p.descripcion}</p>
                    <p class="text-xs text-gray-500 mt-2">
                        <i class="fas fa-tag mr-1"></i>${p.nombreCategoria}
                    </p>
                    <div class="mt-4 flex justify-between items-center">
                        <span class="text-2xl font-bold text-blue-600">${formatCurrency(p.precio)}</span>
                        ${p.disponible ? `
                            <button onclick="Views.catalogo.agregarAlCarrito(${p.id})" 
                                class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
                                <i class="fas fa-cart-plus"></i>
                            </button>
                        ` : `
                            <span class="text-red-500 text-sm font-semibold">
                                <i class="fas fa-times-circle mr-1"></i>Agotado
                            </span>
                        `}
                    </div>
                    <p class="text-xs text-gray-500 mt-2">
                        <i class="fas fa-warehouse mr-1"></i>Stock: ${p.disponibilidad}
                    </p>
                </div>
            </div>
        `).join('');
    },

    setupFilters() {
        const searchInput = document.getElementById('searchInput');
        const categoriaFilter = document.getElementById('categoriaFilter');
        const tipoFilter = document.getElementById('tipoFilter');

        const aplicarFiltros = debounce(() => {
            const searchTerm = searchInput.value.toLowerCase();
            const categoriaId = categoriaFilter.value;
            const tipo = tipoFilter.value;

            let productosFiltrados = this.productos;

            // Filtrar por búsqueda
            if (searchTerm) {
                productosFiltrados = productosFiltrados.filter(p =>
                    p.nombre.toLowerCase().includes(searchTerm) ||
                    p.descripcion.toLowerCase().includes(searchTerm)
                );
            }

            // Filtrar por categoría
            if (categoriaId) {
                productosFiltrados = productosFiltrados.filter(p =>
                    p.nombreCategoria === this.categorias.find(c => c.id == categoriaId)?.nombre
                );
            }

            // Filtrar por tipo
            if (tipo) {
                productosFiltrados = productosFiltrados.filter(p => p.tipoProducto === tipo);
            }

            document.getElementById('productosGrid').innerHTML = this.renderProductos(productosFiltrados);
        }, 300);

        searchInput.addEventListener('input', aplicarFiltros);
        categoriaFilter.addEventListener('change', aplicarFiltros);
        tipoFilter.addEventListener('change', aplicarFiltros);
    },

    async agregarAlCarrito(idProducto) {
        if (!App.currentUser) {
            showToast('Debes iniciar sesión para agregar al carrito', 'warning');
            App.navigateTo('login');
            return;
        }

        try {
            const result = await API.carrito.agregarItem({ idProducto, cantidad: 1 });
            App.cart = result.data;
            App.updateCartCount();
            showToast('Producto agregado al carrito', 'success');
        } catch (error) {
            showToast(error.message || 'Error al agregar al carrito', 'error');
        }
    },

    async verDetalle(idProducto) {
        try {
            const result = await API.catalogo.obtenerDetalle(idProducto);
            const producto = result.data;

            const modalContent = `
                <div class="space-y-4">
                    <img src="${producto.imagenUrl || CONFIG.DEFAULT_IMAGE}" 
                        class="w-full h-64 object-cover rounded-lg">
                    
                    <div>
                        <span class="text-xs ${producto.tipoProducto === 'DIGITAL' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'} px-3 py-1 rounded">
                            ${producto.tipoProducto}
                        </span>
                        <h3 class="text-2xl font-bold mt-2">${producto.nombre}</h3>
                        <p class="text-gray-600 mt-2">${producto.descripcion}</p>
                    </div>

                    <div class="border-t pt-4">
                        <p class="text-gray-600">
                            <i class="fas fa-tag mr-2"></i>
                            <strong>Categoría:</strong> ${producto.nombreCategoria}
                        </p>
                        <p class="text-gray-600 mt-2">
                            <i class="fas fa-warehouse mr-2"></i>
                            <strong>Disponibilidad:</strong> ${producto.disponibilidad} unidades
                        </p>
                    </div>

                    <div class="border-t pt-4 flex justify-between items-center">
                        <span class="text-3xl font-bold text-blue-600">${formatCurrency(producto.precio)}</span>
                        ${producto.disponible ? `
                            <button onclick="Views.catalogo.agregarAlCarrito(${producto.id}); closeModal();" 
                                class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700">
                                <i class="fas fa-cart-plus mr-2"></i>Agregar al Carrito
                            </button>
                        ` : `
                            <span class="text-red-500 font-semibold">No disponible</span>
                        `}
                    </div>
                </div>
            `;

            showModal(`Detalle del Producto`, modalContent, 'max-w-3xl');
        } catch (error) {
            showToast('Error al cargar detalle del producto', 'error');
        }
    }
};