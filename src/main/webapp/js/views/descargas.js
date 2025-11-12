
Views.descargas = {
    async render() {
        if (!App.currentUser) {
            App.navigateTo('login');
            return;
        }

        const content = document.getElementById('mainContent');
        content.innerHTML = Utils.showLoader();

        try {
            const result = await API.getDescargas();
            const descargas = result.data || [];

            content.innerHTML = `
                <div class="animate-fade-in">
                    <h2 class="text-3xl font-bold mb-6">Mis Descargas</h2>
                    
                    ${descargas.length === 0 ? `
                        <div class="text-center py-20">
                            <i class="fas fa-download text-6xl text-gray-300 mb-4"></i>
                            <p class="text-gray-600">No tienes productos digitales para descargar</p>
                        </div>
                    ` : `
                        <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                            ${descargas.map(d => this.renderDescargaCard(d)).join('')}
                        </div>
                    `}
                </div>
            `;
        } catch (error) {
            content.innerHTML = `<div class="text-center py-20"><p class="text-red-500">Error al cargar descargas</p></div>`;
        }
    },

    renderDescargaCard(descarga) {
        return `
            <div class="bg-white rounded-lg shadow-lg overflow-hidden">
                <img src="${descarga.imagenProducto || 'https://via.placeholder.com/300'}" 
                    class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="font-bold text-lg">${descarga.nombreProducto}</h3>
                    <p class="text-sm text-gray-600 mt-2">
                        Descargas: ${descarga.numeroDescargas} / ${descarga.limiteDescargas}
                    </p>
                    <p class="text-xs text-gray-500 mt-1">
                        Comprado: ${Utils.formatDate(descarga.fechaCompra)}
                    </p>
                    ${descarga.puedeDescargar ? `
                        <a href="${descarga.archivoUrl}" target="_blank"
                            onclick="DescargasView.registrarDescarga(${descarga.idProducto})"
                            class="mt-4 w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 inline-block text-center">
                            <i class="fas fa-download mr-2"></i> Descargar
                        </a>
                    ` : `
                        <button disabled
                            class="mt-4 w-full bg-gray-300 text-gray-600 py-2 rounded cursor-not-allowed">
                            Límite Alcanzado
                        </button>
                    `}
                </div>
            </div>
        `;
    },

    async registrarDescarga(idProducto) {
        try {
            await API.registrarDescarga(idProducto);
        } catch (error) {
            console.error('Error al registrar descarga:', error);
        }
    }
};

