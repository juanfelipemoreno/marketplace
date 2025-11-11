
Views.carrito = {
    async render() {
        if (!App.currentUser) {
            App.navigateTo('login');
            return;
        }

        const content = document.getElementById('mainContent');
        showLoader();

        try {
            const result = await API.carrito.obtener();
            const carrito = result.data;

            if (!carrito.items || carrito.items.length === 0) {
                this.renderEmpty();
                return;
            }

            this.renderCarrito(carrito);
        } catch (error) {
            content.innerHTML = `
                <div class="text-center py-20">
                    <p class="text-red-500">Error al cargar el carrito</p>
                </div>
            `;
        }
    },

    renderEmpty() {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="text-center py-20 animate-fade-in">
                <i class="fas fa-shopping-cart text-6xl text-gray-300 mb-4"></i>
                <h2 class="text-2xl font-bold text-gray-600 mb-4">Tu carrito está vacío</h2>
                <p class="text-gray-500 mb-6">¡Descubre productos increíbles en nuestro catálogo!</p>
                <a href="#catalogo" class="bg-blue-600 text-white px-8 py-3 rounded-lg hover:bg-blue-700 inline-block">
                    <i class="fas fa-shopping-bag mr-2"></i>Ir al Catálogo
                </a>
            </div>
        `;
    },

    renderCarrito(carrito) {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="animate-fade-in">
                <h2 class="text-3xl font-bold mb-6">Mi Carrito</h2>
                
                <div class="grid lg:grid-cols-3 gap-6">
                    <!-- Items del carrito -->
                    <div class="lg:col-span-2 space-y-4">
                        ${carrito.items.map(item => this.renderItem(item)).join('')}
                    </div>

                    <!-- Resumen -->
                    <div class="lg:col-span-1">
                        <div class="bg-white p-6 rounded-lg shadow-lg sticky top-24">
                            <h3 class="text-xl font-bold mb-4">Resumen de Compra</h3>
                            <div class="space-y-2 mb-4">
                                <div class="flex justify-between">
                                    <span class="text-gray-600">Subtotal:</span>
                                    <span class="font-bold">${formatCurrency(carrito.totalPrecio)}</span>
                                </div>
                                <div class="flex justify-between">
                                    <span class="text-gray-600">Items:</span>
                                    <span class="font-bold">${carrito.totalItems}</span>
                                </div>
                            </div>
                            <div class="border-t pt-4 mb-4">
                                <div class="flex justify-between text-xl font-bold">
                                    <span>Total:</span>
                                    <span class="text-blue-600">${formatCurrency(carrito.totalPrecio)}</span>
                                </div>
                            </div>
                            <button onclick="Views.carrito.procesarCompra()" 
                                class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 mb-2">
                                <i class="fas fa-credit-card mr-2"></i>Proceder al Pago
                            </button>
                            <button onclick="Views.carrito.vaciar()" 
                                class="w-full bg-red-100 text-red-600 py-2 rounded-lg hover:bg-red-200">
                                <i class="fas fa-trash mr-2"></i>Vaciar Carrito
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    },

    renderItem(item) {
        return `
            <div class="bg-white p-4 rounded-lg shadow hover:shadow-lg transition flex items-center space-x-4">
                <img src="${item.imagenProducto || CONFIG.DEFAULT_IMAGE}" 
                    class="w-24 h-24 object-cover rounded">
                <div class="flex-1">
                    <h3 class="font-bold text-lg">${item.nombreProducto}</h3>
                    <p class="text-gray-600">${formatCurrency(item.precioUnitario)}</p>
                    <span class="text-xs ${item.tipoProducto === 'DIGITAL' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'} px-2 py-1 rounded">
                        ${item.tipoProducto}
                    </span>
                    <p class="text-xs text-gray-500 mt-1">
                        <i class="fas fa-warehouse mr-1"></i>Disponible: ${item.disponibilidad}
                    </p>
                </div>
                <div class="flex items-center space-x-3">
                    <button onclick="Views.carrito.actualizarCantidad(${item.idItem}, ${item.cantidad - 1})"
                        class="bg-gray-200 w-8 h-8 rounded hover:bg-gray-300 flex items-center justify-center">
                        <i class="fas fa-minus text-sm"></i>
                    </button>
                    <span class="font-bold text-lg w-8 text-center">${item.cantidad}</span>
                    <button onclick="Views.carrito.actualizarCantidad(${item.idItem}, ${item.cantidad + 1})"
                        class="bg-gray-200 w-8 h-8 rounded hover:bg-gray-300 flex items-center justify-center">
                        <i class="fas fa-plus text-sm"></i>
                    </button>
                </div>
                <div class="text-right">
                    <p class="font-bold text-xl text-blue-600">${formatCurrency(item.subtotal)}</p>
                    <button onclick="Views.carrito.eliminarItem(${item.idItem})"
                        class="text-red-500 hover:text-red-700 text-sm mt-2">
                        <i class="fas fa-trash mr-1"></i>Eliminar
                    </button>
                </div>
            </div>
        `;
    },

    async actualizarCantidad(idItem, nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            this.eliminarItem(idItem);
            return;
        }

        try {
            const result = await API.carrito.actualizarCantidad(idItem, nuevaCantidad);
            App.cart = result.data;
            App.updateCartCount();
            this.render();
        } catch (error) {
            showToast(error.message || 'Error al actualizar cantidad', 'error');
        }
    },

    async eliminarItem(idItem) {
        try {
            const result = await API.carrito.eliminarItem(idItem);
            App.cart = result.data;
            App.updateCartCount();
            this.render();
            showToast('Producto eliminado del carrito', 'success');
        } catch (error) {
            showToast(error.message || 'Error al eliminar', 'error');
        }
    },

    async vaciar() {
        if (!confirm('¿Estás seguro de vaciar el carrito?')) return;

        try {
            await API.carrito.vaciar();
            App.cart = null;
            App.updateCartCount();
            this.renderEmpty();
            showToast('Carrito vaciado', 'success');
        } catch (error) {
            showToast('Error al vaciar el carrito', 'error');
        }
    },

    procesarCompra() {
        const modalContent = `
            <form id="checkoutForm" class="space-y-4">
                <div>
                    <label class="block text-gray-700 font-semibold mb-2">Método de Pago</label>
                    <select name="metodoPago" required 
                        class="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-blue-500">
                        <option value="">Seleccionar...</option>
                        <option value="TARJETA_CREDITO">💳 Tarjeta de Crédito</option>
                        <option value="TARJETA_DEBITO">💳 Tarjeta de Débito</option>
                        <option value="PSE">🏦 PSE - Pago Seguro en Línea</option>
                        <option value="EFECTIVO">💵 Efectivo contra entrega</option>
                        <option value="TRANSFERENCIA">🏦 Transferencia Bancaria</option>
                    </select>
                </div>
                
                <div class="bg-blue-50 p-4 rounded-lg">
                    <p class="text-sm text-blue-800">
                        <i class="fas fa-info-circle mr-2"></i>
                        Este es un pago simulado. No se realizará ningún cargo real.
                    </p>
                </div>

                <button type="submit" 
                    class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700">
                    <i class="fas fa-check-circle mr-2"></i>Confirmar Compra
                </button>
            </form>
        `;

        showModal('Finalizar Compra', modalContent);

        document.getElementById('checkoutForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);

            try {
                const result = await API.ordenes.crear({
                    metodoPago: formData.get('metodoPago')
                });

                closeModal();
                App.cart = null;
                App.updateCartCount();
                showToast('¡Compra realizada exitosamente!', 'success');
                
                // Mostrar detalles de la orden
                setTimeout(() => {
                    this.mostrarConfirmacion(result.data);
                }, 500);
            } catch (error) {
                showToast(error.message || 'Error al procesar compra', 'error');
            }
        });
    },

    mostrarConfirmacion(orden) {
        const modalContent = `
            <div class="text-center space-y-4">
                <div class="inline-block bg-green-100 p-4 rounded-full">
                    <i class="fas fa-check-circle text-green-600 text-5xl"></i>
                </div>
                <h3 class="text-2xl font-bold">¡Compra Exitosa!</h3>
                <p class="text-gray-600">Tu orden ha sido procesada correctamente</p>
                
                <div class="bg-gray-50 p-4 rounded-lg text-left">
                    <p><strong>Número de Orden:</strong> ${orden.numeroOrden}</p>
                    <p><strong>Total:</strong> ${formatCurrency(orden.total)}</p>
                    <p><strong>Método de Pago:</strong> ${orden.metodoPago}</p>
                </div>

                <div class="flex gap-4">
                    <button onclick="closeModal(); App.navigateTo('ordenes')" 
                        class="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700">
                        Ver Mis Órdenes
                    </button>
                    <button onclick="closeModal(); App.navigateTo('catalogo')" 
                        class="flex-1 bg-gray-200 text-gray-700 py-2 rounded-lg hover:bg-gray-300">
                        Seguir Comprando
                    </button>
                </div>
            </div>
        `;

        showModal('Confirmación de Compra', modalContent);
    }
};