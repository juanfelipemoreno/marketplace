
Views.productos = {
    async render() {
        if (!App.currentUser || (App.currentUser.rol !== 'VENDEDOR' && App.currentUser.rol !== 'ADMIN')) {
            App.navigateTo('home');
            return;
        }

        const content = document.getElementById('mainContent');
        content.innerHTML = Utils.showLoader();

        try {
            const result = await API.getProductos();
            const productos = result.data || [];

            content.innerHTML = `
                <div class="animate-fade-in">
                    <div class="flex justify-between items-center mb-6">
                        <h2 class="text-3xl font-bold">Mis Productos</h2>
                        <button onclick="ProductosView.mostrarCrearProducto()" 
                            class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700">
                            <i class="fas fa-plus mr-2"></i> Nuevo Producto
                        </button>
                    </div>

                    ${productos.length === 0 ? `
                        <div class="text-center py-20">
                            <i class="fas fa-box-open text-6xl text-gray-300 mb-4"></i>
                            <p class="text-gray-600">No tienes productos aún</p>
                        </div>
                    ` : `
                        <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                            ${productos.map(p => this.renderProductCard(p)).join('')}
                        </div>
                    `}
                </div>
            `;
        } catch (error) {
            content.innerHTML = `<div class="text-center py-20"><p class="text-red-500">Error al cargar productos</p></div>`;
        }
    },

    renderProductCard(producto) {
        return `
            <div class="bg-white rounded-lg shadow-lg overflow-hidden">
                <img src="${producto.imagenUrl || 'https://via.placeholder.com/300'}" 
                    class="w-full h-48 object-cover">
                <div class="p-4">
                    <div class="flex justify-between items-start mb-2">
                        <span class="text-xs ${producto.tipoProducto === 'DIGITAL' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'} px-2 py-1 rounded">
                            ${producto.tipoProducto}
                        </span>
                        <span class="text-xs ${producto.estado ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'} px-2 py-1 rounded">
                            ${producto.estado ? 'Activo' : 'Inactivo'}
                        </span>
                    </div>
                    <h3 class="font-bold text-lg">${producto.nombre}</h3>
                    <p class="text-gray-600 text-sm mt-1">${producto.descripcion}</p>
                    <div class="mt-4 flex justify-between items-center">
                        <span class="text-xl font-bold text-blue-600">${Utils.formatCurrency(producto.precio)}</span>
                        <span class="text-sm text-gray-600">Stock: ${producto.disponibilidad}</span>
                    </div>
                    <div class="mt-4 flex space-x-2">
                        <button onclick="ProductosView.toggleEstado(${producto.id}, ${!producto.estado})" 
                            class="flex-1 ${producto.estado ? 'bg-red-500 hover:bg-red-600' : 'bg-green-500 hover:bg-green-600'} text-white px-3 py-2 rounded">
                            <i class="fas fa-${producto.estado ? 'times' : 'check'}"></i> ${producto.estado ? 'Desactivar' : 'Activar'}
                        </button>
                    </div>
                </div>
            </div>
        `;
    },

    async mostrarCrearProducto() {
        const categorias = await API.getCategorias();

        const modalContent = `
            <form id="crearProductoForm" class="space-y-4">
                <div class="grid md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-gray-700 mb-2">Nombre *</label>
                        <input type="text" name="nombre" required class="w-full px-4 py-2 border rounded-lg">
                    </div>
                    <div>
                        <label class="block text-gray-700 mb-2">Categoría *</label>
                        <select name="idCategoria" required class="w-full px-4 py-2 border rounded-lg">
                            <option value="">Seleccionar...</option>
                            ${categorias.data.map(c => `<option value="${c.id}">${c.nombre}</option>`).join('')}
                        </select>
                    </div>
                </div>
                
                <div>
                    <label class="block text-gray-700 mb-2">Descripción *</label>
                    <textarea name="descripcion" required rows="3" class="w-full px-4 py-2 border rounded-lg"></textarea>
                </div>

                <div class="grid md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-gray-700 mb-2">Precio *</label>
                        <input type="number" name="precio" step="0.01" required class="w-full px-4 py-2 border rounded-lg">
                    </div>
                    <div>
                        <label class="block text-gray-700 mb-2">Tipo de Producto *</label>
                        <select name="tipoProducto" required onchange="ProductosView.toggleInventarioFields(this.value)" class="w-full px-4 py-2 border rounded-lg">
                            <option value="">Seleccionar...</option>
                            <option value="FISICO">Físico</option>
                            <option value="DIGITAL">Digital</option>
                        </select>
                    </div>
                </div>

                <div>
                    <label class="block text-gray-700 mb-2">URL de Imagen</label>
                    <input type="url" name="imagenUrl" class="w-full px-4 py-2 border rounded-lg">
                </div>

                <div id="inventarioFisico" style="display:none;">
                    <label class="block text-gray-700 mb-2">Cantidad Disponible *</label>
                    <input type="number" name="cantidadDisponible" min="0" class="w-full px-4 py-2 border rounded-lg">
                </div>

                <div id="inventarioDigital" style="display:none;">
                    <div class="space-y-4">
                        <div>
                            <label class="block text-gray-700 mb-2">Licencias Totales *</label>
                            <input type="number" name="licenciasTotales" min="1" class="w-full px-4 py-2 border rounded-lg">
                        </div>
                        <div>
                            <label class="block text-gray-700 mb-2">URL del Archivo *</label>
                            <input type="url" name="archivoUrl" class="w-full px-4 py-2 border rounded-lg">
                        </div>
                    </div>
                </div>

                <button type="submit" class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700">
                    Crear Producto
                </button>
            </form>
        `;

        Utils.showModal('Nuevo Producto', modalContent);

        document.getElementById('crearProductoForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);

            const data = {
                nombre: formData.get('nombre'),
                descripcion: formData.get('descripcion'),
                precio: parseFloat(formData.get('precio')),
                tipoProducto: formData.get('tipoProducto'),
                idCategoria: parseInt(formData.get('idCategoria')),
                imagenUrl: formData.get('imagenUrl') || null
            };

            if (data.tipoProducto === 'FISICO') {
                data.cantidadDisponible = parseInt(formData.get('cantidadDisponible')) || 0;
            } else if (data.tipoProducto === 'DIGITAL') {
                data.licenciasTotales = parseInt(formData.get('licenciasTotales')) || 1;
                data.archivoUrl = formData.get('archivoUrl');
            }

            try {
                await API.createProducto(data);
                Utils.closeModal();
                Utils.showToast('Producto creado exitosamente', 'success');
                this.render();
            } catch (error) {
                Utils.showToast(error.message || 'Error al crear producto', 'error');
            }
        });
    },

    toggleInventarioFields(tipo) {
        document.getElementById('inventarioFisico').style.display = tipo === 'FISICO' ? 'block' : 'none';
        document.getElementById('inventarioDigital').style.display = tipo === 'DIGITAL' ? 'block' : 'none';
    },

    async toggleEstado(idProducto, nuevoEstado) {
        try {
            await API.toggleProductoEstado(idProducto, nuevoEstado);
            Utils.showToast(`Producto ${nuevoEstado ? 'activado' : 'desactivado'} exitosamente`, 'success');
            this.render();
        } catch (error) {
            Utils.showToast('Error al cambiar estado', 'error');
        }
    }
};