
Views.ordenes = {
    async render() {
        if (!App.currentUser) {
            App.navigateTo('login');
            return;
        }

        const content = document.getElementById('mainContent');
        showLoader();

        try {
            const result = await API.ordenes.listarMisOrdenes();
            const ordenes = result.data || [];

            if (ordenes.length === 0) {
                this.renderEmpty();
                return;
            }

            this.renderOrdenes(ordenes);
        } catch (error) {
            content.innerHTML = `
                <div class="text-center py-20">
                    <p class="text-red-500">Error al cargar órdenes</p>
                </div>
            `;
        }
    },

    renderEmpty() {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="text-center py-20 animate-fade-in">
                <i class="fas fa-receipt text-6xl text-gray-300 mb-4"></i>
                <h2 class="text-2xl font-bold text-gray-600 mb-4">No tienes órdenes</h2>
                <p class="text-gray-500 mb-6">Comienza a comprar en nuestro catálogo</p>
                <a href="#catalogo" class="bg-blue-600 text-white px-8 py-3 rounded-lg hover:bg-blue-700 inline-block">
                    <i class="fas fa-shopping-bag mr-2"></i>Ir al Catálogo
                </a>
            </div>
        `;
    },

    renderOrdenes(ordenes) {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="animate-fade-in">
                <h2 class="text-3xl font-bold mb-6">Mis Órdenes</h2>
                
                <div class="space-y-4">
                    ${ordenes.map(orden => this.renderOrdenCard(orden)).join('')}
                </div>
            </div>
        `;
    },

    renderOrdenCard(orden) {
        const estadoClasses = {
            'PENDIENTE': 'bg-yellow-100 text-yellow-800',
            'CONFIRMADA': 'bg-green-100 text-green-800',
            'CANCELADA': 'bg-red-100 text-red-800',
            'COMPLETADA': 'bg-blue-100 text-blue-800'
        };

        return `
            <div class="bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition">
                <div class="flex justify-between items-start mb-4">
                    <div>
                        <h3 class="font-bold text-xl">Orden #${orden.numeroOrden}</h3>
                        <p class="text-gray-600 text-sm">${formatDateTime(orden.fechaOrden)}</p>
                    </div>
                    <span class="px-3 py-1 rounded ${estadoClasses[orden.estado] || 'bg-gray-100 text-gray-800'}">
                        ${orden.estado}
                    </span>
                </div>

                <div class="border-t pt-4 grid md:grid-cols-2 gap-4">
                    <div>
                        <p class="text-gray-600 text-sm">Total</p>
                        <p class="font-bold text-2xl text-blue-600">${formatCurrency(orden.total)}</p>
                    </div>
                    <div>
                        <p class="text-gray-600 text-sm">Método de Pago</p>
                        <p class="font-semibold">${orden.metodoPago || 'N/A'}</p>
                    </div>
                </div>

                <div class="mt-4 flex space-x-2">
                    <button onclick="Views.ordenes.verDetalle(${orden.id})" 
                        class="flex-1 bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
                        <i class="fas fa-eye mr-2"></i>Ver Detalle
                    </button>
                    ${orden.estado === 'PENDIENTE' ? `
                        <button onclick="Views.ordenes.cancelar(${orden.id})" 
                            class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                            <i class="fas fa-times mr-1"></i>Cancelar
                        </button>
                    ` : ''}
                </div>
            </div>
        `;
    },

    async verDetalle(idOrden) {
        try {
            const result = await API.ordenes.obtenerPorId(idOrden);
            const orden = result.data;

            const modalContent = `
                <div class="space-y-4">
                    <div class="bg-gray-50 p-4 rounded-lg">
                        <h3 class="font-bold text-xl mb-2">Orden #${orden.numeroOrden}</h3>
                        <p class="text-gray-600">${formatDateTime(orden.fechaOrden)}</p>
                        <span class="inline-block mt-2 px-3 py-1 rounded bg-${orden.estado === 'CONFIRMADA' ? 'green' : 'yellow'}-100 text-${orden.estado === 'CONFIRMADA' ? 'green' : 'yellow'}-800">
                            ${orden.estado}
                        </span>
                    </div>

                    <div>
                        <h4 class="font-bold mb-3">Productos</h4>
                        <div class="space-y-2">
                            ${orden.detalles.map(detalle => `
                                <div class="flex justify-between items-center p-3 bg-gray-50 rounded">
                                    <div class="flex-1">
                                        <p class="font-semibold">${detalle.nombreProducto}</p>
                                        <p class="text-sm text-gray-600">
                                            ${detalle.cantidad} x ${formatCurrency(detalle.precioUnitario)}
                                        </p>
                                    </div>
                                    <p class="font-bold text-blue-600">${formatCurrency(detalle.subtotal)}</p>
                                </div>
                            `).join('')}
                        </div>
                    </div>

                    <div class="border-t pt-4">
                        <div class="flex justify-between text-xl font-bold">
                            <span>Total:</span>
                            <span class="text-blue-600">${formatCurrency(orden.total)}</span>
                        </div>
                    </div>
                </div>
            `;

            showModal(`Detalle de Orden`, modalContent, 'max-w-3xl');
        } catch (error) {
            showToast('Error al cargar detalle de la orden', 'error');
        }
    },

    async cancelar(idOrden) {
        if (!confirm('¿Estás seguro de cancelar esta orden?')) return;

        try {
            await API.ordenes.cancelar(idOrden);
            showToast('Orden cancelada exitosamente', 'success');
            this.render();
        } catch (error) {
            showToast(error.message || 'Error al cancelar orden', 'error');
        }
    }
};

