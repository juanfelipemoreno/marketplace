
Views.admin = {
    async render() {
        if (!App.currentUser || App.currentUser.rol !== 'ADMIN') {
            App.navigateTo('home');
            return;
        }

        const content = document.getElementById('mainContent');
        content.innerHTML = Utils.showLoader();

        try {
            const result = await API.getAdminDashboard();
            const dashboard = result.data;

            content.innerHTML = `
                <div class="animate-fade-in">
                    <h2 class="text-3xl font-bold mb-6">Panel de Administración</h2>
                    
                    <div class="grid md:grid-cols-4 gap-6 mb-8">
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                            <i class="fas fa-users text-blue-600 text-3xl mb-2"></i>
                            <h3 class="text-gray-600">Total Usuarios</h3>
                            <p class="text-3xl font-bold">${dashboard.totalUsuarios}</p>
                        </div>
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                            <i class="fas fa-user-tie text-purple-600 text-3xl mb-2"></i>
                            <h3 class="text-gray-600">Vendedores</h3>
                            <p class="text-3xl font-bold">${dashboard.totalVendedores}</p>
                        </div>
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                            <i class="fas fa-shopping-cart text-green-600 text-3xl mb-2"></i>
                            <h3 class="text-gray-600">Compradores</h3>
                            <p class="text-3xl font-bold">${dashboard.totalCompradores}</p>
                        </div>
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                            <i class="fas fa-receipt text-orange-600 text-3xl mb-2"></i>
                            <h3 class="text-gray-600">Total Órdenes</h3>
                            <p class="text-3xl font-bold">${dashboard.totalOrdenes}</p>
                        </div>
                    </div>

                    <div class="grid md:grid-cols-2 gap-6">
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                            <h3 class="text-xl font-bold mb-4">Gestión de Usuarios</h3>
                            <button onclick="AdminView.verUsuarios()" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
                                <i class="fas fa-users mr-2"></i> Ver Todos los Usuarios
                            </button>
                        </div>
                        
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                            <h3 class="text-xl font-bold mb-4">Gestión de Categorías</h3>
                            <button onclick="AdminView.verCategorias()" class="w-full bg-purple-600 text-white py-2 rounded hover:bg-purple-700">
                                <i class="fas fa-tags mr-2"></i> Ver Todas las Categorías
                            </button>
                        </div>
                    </div>
                </div>`;         
        } catch (error) {             
            content.innerHTML = `<div class="text-center py-20"><p class="text-red-500">Error al cargar dashboard</p></div>`;
        }
    },
    
    async verUsuarios() {
        try {
            const result = await API.getUsuarios();
            const usuarios = result.data || [];

            const modalContent = `
                <div class="space-y-4 max-h-96 overflow-y-auto">
                    <div class="mb-4">
                        <input type="text" id="searchUsuarios" placeholder="Buscar usuarios..."
                            class="w-full px-4 py-2 border rounded-lg">
                    </div>
                    <div id="listaUsuarios">
                        ${usuarios.map(u => this.renderUsuarioCard(u)).join('')}
                    </div>
                </div>
            `;

            Utils.showModal('Gestión de Usuarios', modalContent);

            // Búsqueda en tiempo real
            document.getElementById('searchUsuarios').addEventListener('input', (e) => {
                const term = e.target.value.toLowerCase();
                const filtered = usuarios.filter(u =>
                    u.nombre.toLowerCase().includes(term) ||
                    u.apellido.toLowerCase().includes(term) ||
                    u.email.toLowerCase().includes(term)
                );
                document.getElementById('listaUsuarios').innerHTML = 
                    filtered.map(u => this.renderUsuarioCard(u)).join('');
            });

        } catch (error) {
            Utils.showToast('Error al cargar usuarios', 'error');
        }
    },

    renderUsuarioCard(usuario) {
        return `
            <div class="flex justify-between items-center p-4 bg-gray-50 rounded mb-2">
                <div class="flex-1">
                    <p class="font-bold">${usuario.nombre} ${usuario.apellido}</p>
                    <p class="text-sm text-gray-600">${usuario.email}</p>
                    <span class="text-xs px-2 py-1 rounded ${
                        usuario.rol === 'ADMIN' ? 'bg-red-100 text-red-800' :
                        usuario.rol === 'VENDEDOR' ? 'bg-blue-100 text-blue-800' :
                        'bg-green-100 text-green-800'
                    }">
                        ${usuario.rol}
                    </span>
                    <span class="text-xs px-2 py-1 rounded ml-2 ${usuario.estado ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">
                        ${usuario.estado ? 'Activo' : 'Inactivo'}
                    </span>
                </div>
                <div class="flex space-x-2">
                    <select onchange="AdminView.cambiarRol(${usuario.idUsuario}, this.value)"
                        class="px-2 py-1 border rounded text-sm">
                        <option value="${usuario.rol}">${usuario.rol}</option>
                        ${usuario.rol !== 'ADMIN' ? '<option value="ADMIN">ADMIN</option>' : ''}
                        ${usuario.rol !== 'VENDEDOR' ? '<option value="VENDEDOR">VENDEDOR</option>' : ''}
                        ${usuario.rol !== 'COMPRADOR' ? '<option value="COMPRADOR">COMPRADOR</option>' : ''}
                    </select>
                    <button onclick="AdminView.toggleEstadoUsuario(${usuario.idUsuario}, ${!usuario.estado})"
                        class="${usuario.estado ? 'bg-red-500' : 'bg-green-500'} text-white px-3 py-1 rounded text-sm hover:opacity-80">
                        ${usuario.estado ? 'Desactivar' : 'Activar'}
                    </button>
                </div>
            </div>
        `;
    },

    async cambiarRol(idUsuario, nuevoRol) {
        try {
            await API.cambiarRolUsuario(idUsuario, nuevoRol);
            Utils.showToast('Rol actualizado exitosamente', 'success');
            Utils.closeModal();
            this.verUsuarios();
        } catch (error) {
            Utils.showToast('Error al cambiar rol', 'error');
        }
    },

    async toggleEstadoUsuario(idUsuario, nuevoEstado) {
        try {
            await API.toggleUsuarioEstado(idUsuario, nuevoEstado);
            Utils.showToast(`Usuario ${nuevoEstado ? 'activado' : 'desactivado'} exitosamente`, 'success');
            Utils.closeModal();
            this.verUsuarios();
        } catch (error) {
            Utils.showToast('Error al cambiar estado', 'error');
        }
    },

    async verCategorias() {
        try {
            const response = await fetch('/marketplace-backend/resources/admin/categorias');
            const result = await response.json();
            const categorias = result.data || [];

            const modalContent = `
                <div class="space-y-4">
                    <button onclick="AdminView.mostrarCrearCategoria()" 
                        class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
                        <i class="fas fa-plus mr-2"></i> Nueva Categoría
                    </button>

                    <div class="max-h-96 overflow-y-auto">
                        ${categorias.map(c => this.renderCategoriaCard(c)).join('')}
                    </div>
                </div>
            `;

            Utils.showModal('Gestión de Categorías', modalContent);

        } catch (error) {
            Utils.showToast('Error al cargar categorías', 'error');
        }
    },

    renderCategoriaCard(categoria) {
        return `
            <div class="flex justify-between items-center p-4 bg-gray-50 rounded mb-2">
                <div class="flex-1">
                    <p class="font-bold">${categoria.nombre}</p>
                    <p class="text-sm text-gray-600">${categoria.descripcion || 'Sin descripción'}</p>
                    <span class="text-xs px-2 py-1 rounded mt-1 inline-block ${categoria.estado ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">
                        ${categoria.estado ? 'Activa' : 'Inactiva'}
                    </span>
                </div>
                <div class="flex space-x-2">
                    <button onclick="AdminView.editarCategoria(${categoria.id})"
                        class="bg-yellow-500 text-white px-3 py-1 rounded text-sm hover:bg-yellow-600">
                        Editar
                    </button>
                    <button onclick="AdminView.toggleEstadoCategoria(${categoria.id}, ${!categoria.estado})"
                        class="${categoria.estado ? 'bg-red-500' : 'bg-green-500'} text-white px-3 py-1 rounded text-sm hover:opacity-80">
                        ${categoria.estado ? 'Desactivar' : 'Activar'}
                    </button>
                </div>
            </div>
        `;
    },

    async mostrarCrearCategoria() {
        const modalContent = `
            <form id="crearCategoriaForm" class="space-y-4">
                <div>
                    <label class="block text-gray-700 mb-2">Nombre *</label>
                    <input type="text" name="nombre" required 
                        class="w-full px-4 py-2 border rounded-lg">
                </div>
                <div>
                    <label class="block text-gray-700 mb-2">Descripción</label>
                    <textarea name="descripcion" rows="3"
                        class="w-full px-4 py-2 border rounded-lg"></textarea>
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700">
                    Crear Categoría
                </button>
            </form>
        `;

        Utils.showModal('Nueva Categoría', modalContent);

        document.getElementById('crearCategoriaForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);

            try {
                await API.createCategoria({
                    nombre: formData.get('nombre'),
                    descripcion: formData.get('descripcion')
                });

                Utils.closeModal();
                Utils.showToast('Categoría creada exitosamente', 'success');
                this.verCategorias();
            } catch (error) {
                Utils.showToast(error.message || 'Error al crear categoría', 'error');
            }
        });
    },

    async editarCategoria(idCategoria) {
        try {
            const response = await fetch(`/marketplace-backend/resources/categorias/${idCategoria}`);
            const result = await response.json();
            const categoria = result.data;

            const modalContent = `
                <form id="editarCategoriaForm" class="space-y-4">
                    <div>
                        <label class="block text-gray-700 mb-2">Nombre *</label>
                        <input type="text" name="nombre" value="${categoria.nombre}" required 
                            class="w-full px-4 py-2 border rounded-lg">
                    </div>
                    <div>
                        <label class="block text-gray-700 mb-2">Descripción</label>
                        <textarea name="descripcion" rows="3"
                            class="w-full px-4 py-2 border rounded-lg">${categoria.descripcion || ''}</textarea>
                    </div>
                    <button type="submit" class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700">
                        Actualizar Categoría
                    </button>
                </form>
            `;

            Utils.showModal('Editar Categoría', modalContent);

            document.getElementById('editarCategoriaForm').addEventListener('submit', async (e) => {
                e.preventDefault();
                const formData = new FormData(e.target);

                try {
                    await API.updateCategoria(idCategoria, {
                        nombre: formData.get('nombre'),
                        descripcion: formData.get('descripcion')
                    });

                    Utils.closeModal();
                    Utils.showToast('Categoría actualizada exitosamente', 'success');
                    this.verCategorias();
                } catch (error) {
                    Utils.showToast(error.message || 'Error al actualizar categoría', 'error');
                }
            });

        } catch (error) {
            Utils.showToast('Error al cargar categoría', 'error');
        }
    },

    async toggleEstadoCategoria(idCategoria, nuevoEstado) {
        try {
            const method = nuevoEstado ? 'PUT' : 'DELETE';
            await fetch(`/marketplace-backend/resources/categorias/${idCategoria}`, { method });

            Utils.showToast(`Categoría ${nuevoEstado ? 'activada' : 'desactivada'} exitosamente`, 'success');
            Utils.closeModal();
            this.verCategorias();
        } catch (error) {
            Utils.showToast('Error al cambiar estado', 'error');
        }
    }
};