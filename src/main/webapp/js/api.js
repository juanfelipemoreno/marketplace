
const API = {
 
    async request(endpoint, options = {}) {
        const url = `${CONFIG.API_BASE}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        try {
            const response = await fetch(url, config);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Error en la petición');
            }

            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    usuarios: {
        async registrar(datos) {
            return await API.request('/usuarios/registro', {
                method: 'POST',
                body: JSON.stringify(datos)
            });
        },

        async login(datos) {
            return await API.request('/usuarios/login', {
                method: 'POST',
                body: JSON.stringify(datos)
            });
        },

        async logout() {
            return await API.request('/usuarios/logout', { method: 'POST' });
        },

        async obtenerPerfil() {
            return await API.request('/usuarios/perfil');
        },

        async actualizarPerfil(datos) {
            return await API.request('/usuarios/perfil', {
                method: 'PUT',
                body: JSON.stringify(datos)
            });
        },

        async cambiarPassword(datos) {
            return await API.request('/usuarios/password', {
                method: 'PUT',
                body: JSON.stringify(datos)
            });
        },

        async verificarEmail(email) {
            return await API.request(`/usuarios/verificar-email?email=${encodeURIComponent(email)}`);
        }
    },


    productos: {
        async crear(datos) {
            return await API.request('/productos', {
                method: 'POST',
                body: JSON.stringify(datos)
            });
        },

        async actualizar(id, datos) {
            return await API.request(`/productos/${id}`, {
                method: 'PUT',
                body: JSON.stringify(datos)
            });
        },

        async obtenerPorId(id) {
            return await API.request(`/productos/${id}`);
        },

        async listarMisProductos() {
            return await API.request('/productos/mis-productos');
        },

        async activar(id) {
            return await API.request(`/productos/${id}/activar`, { method: 'PUT' });
        },

        async desactivar(id) {
            return await API.request(`/productos/${id}`, { method: 'DELETE' });
        }
    },

    catalogo: {
        async listar() {
            return await API.request('/catalogo');
        },

        async buscar(termino) {
            return await API.request(`/catalogo/buscar?q=${encodeURIComponent(termino)}`);
        },

        async filtrarPorCategoria(idCategoria) {
            return await API.request(`/catalogo/categoria/${idCategoria}`);
        },

        async filtrarPorTipo(tipo) {
            return await API.request(`/catalogo/tipo/${tipo}`);
        },

        async obtenerDetalle(id) {
            return await API.request(`/catalogo/producto/${id}`);
        }
    },

    carrito: {
        async obtener() {
            return await API.request('/carrito');
        },

        async agregarItem(datos) {
            return await API.request('/carrito/items', {
                method: 'POST',
                body: JSON.stringify(datos)
            });
        },

        async actualizarCantidad(idItem, cantidad) {
            return await API.request(`/carrito/items/${idItem}?cantidad=${cantidad}`, {
                method: 'PUT'
            });
        },

        async eliminarItem(idItem) {
            return await API.request(`/carrito/items/${idItem}`, { method: 'DELETE' });
        },

        async vaciar() {
            return await API.request('/carrito', { method: 'DELETE' });
        },

        async validar() {
            return await API.request('/carrito/validar');
        }
    },

    ordenes: {
        async crear(datos) {
            return await API.request('/ordenes', {
                method: 'POST',
                body: JSON.stringify(datos)
            });
        },

        async listarMisOrdenes() {
            return await API.request('/ordenes');
        },

        async obtenerPorId(id) {
            return await API.request(`/ordenes/${id}`);
        },

        async obtenerPorNumero(numeroOrden) {
            return await API.request(`/ordenes/numero/${numeroOrden}`);
        },

        async listarVentas() {
            return await API.request('/ordenes/ventas');
        },

        async cancelar(id) {
            return await API.request(`/ordenes/${id}/cancelar`, { method: 'PUT' });
        }
    },

    categorias: {
        async listarActivas() {
            return await API.request('/categorias');
        },

        async listarTodas() {
            return await API.request('/categorias/todas');
        },

        async obtenerPorId(id) {
            return await API.request(`/categorias/${id}`);
        },

        async crear(datos) {
            return await API.request('/categorias', {
                method: 'POST',
                body: JSON.stringify(datos)
            });
        },

        async actualizar(id, datos) {
            return await API.request(`/categorias/${id}`, {
                method: 'PUT',
                body: JSON.stringify(datos)
            });
        },

        async desactivar(id) {
            return await API.request(`/categorias/${id}`, { method: 'DELETE' });
        }
    },

    descargas: {
        async listar() {
            return await API.request('/descargas');
        },

        async listarTodas() {
            return await API.request('/descargas/todas');
        },

        async obtenerInfo(idProducto) {
            return await API.request(`/descargas/${idProducto}/info`);
        },

        async registrar(idProducto) {
            return await API.request(`/descargas/${idProducto}/registrar`, { method: 'POST' });
        },

        async puedeDescargar(idProducto) {
            return await API.request(`/descargas/${idProducto}/puede-descargar`);
        }
    },

    admin: {
        async obtenerDashboard() {
            return await API.request('/admin/dashboard');
        },

        async listarUsuarios() {
            return await API.request('/admin/usuarios');
        },

        async obtenerUsuario(id) {
            return await API.request(`/admin/usuarios/${id}`);
        },

        async cambiarRol(id, rol) {
            return await API.request(`/admin/usuarios/${id}/rol?rol=${rol}`, { method: 'PUT' });
        },

        async cambiarEstadoUsuario(id, estado) {
            return await API.request(`/admin/usuarios/${id}/estado?estado=${estado}`, {
                method: 'PUT'
            });
        },

        async listarCategorias() {
            return await API.request('/admin/categorias');
        },

        async listarOrdenes() {
            return await API.request('/admin/ordenes');
        },

        async reporteVentas() {
            return await API.request('/admin/reportes/ventas');
        },

        async reporteInventario() {
            return await API.request('/admin/reportes/inventario');
        }
    }
};