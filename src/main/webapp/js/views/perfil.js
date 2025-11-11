
const PerfilView = {
    async render() {
        if (!App.currentUser) {
            App.navigateTo('login');
            return;
        }

        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="max-w-2xl mx-auto animate-fade-in">
                <h2 class="text-3xl font-bold mb-6">Mi Perfil</h2>
                
                <div class="bg-white p-6 rounded-lg shadow-lg">
                    <div class="flex items-center space-x-4 mb-6 pb-6 border-b">
                        <div class="w-20 h-20 bg-blue-600 text-white rounded-full flex items-center justify-center text-3xl font-bold">
                            ${App.currentUser.nombre.charAt(0)}${App.currentUser.apellido.charAt(0)}
                        </div>
                        <div>
                            <h3 class="text-2xl font-bold">${App.currentUser.nombre} ${App.currentUser.apellido}</h3>
                            <p class="text-gray-600">${App.currentUser.email}</p>
                            <span class="inline-block mt-1 px-3 py-1 bg-blue-100 text-blue-800 rounded text-sm">
                                ${App.currentUser.rol}
                            </span>
                        </div>
                    </div>

                    <div class="space-y-4">
                        <div>
                            <label class="block text-gray-700 mb-2">Fecha de Registro</label>
                            <p class="text-gray-600">${Utils.formatDate(App.currentUser.fechaRegistro)}</p>
                        </div>
                        
                        <div>
                            <label class="block text-gray-700 mb-2">Estado de Cuenta</label>
                            <span class="px-3 py-1 rounded ${App.currentUser.estado ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">
                                ${App.currentUser.estado ? 'Activa' : 'Inactiva'}
                            </span>
                        </div>
                    </div>

                    <div class="mt-6 pt-6 border-t space-x-4">
                        <button onclick="PerfilView.mostrarCambiarPassword()" 
                            class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700">
                            Cambiar Contraseña
                        </button>
                    </div>
                </div>
            </div>
        `;
    },

    mostrarCambiarPassword() {
        const modalContent = `
            <form id="cambiarPasswordForm" class="space-y-4">
                <div>
                    <label class="block text-gray-700 mb-2">Contraseña Actual</label>
                    <input type="password" name="passwordActual" required 
                        class="w-full px-4 py-2 border rounded-lg">
                </div>
                <div>
                    <label class="block text-gray-700 mb-2">Nueva Contraseña</label>
                    <input type="password" name="passwordNueva" required 
                        class="w-full px-4 py-2 border rounded-lg">
                </div>
                <button type="submit" class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700">
                    Cambiar Contraseña
                </button>
            </form>
        `;

        Utils.showModal('Cambiar Contraseña', modalContent);

        document.getElementById('cambiarPasswordForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);

            try {
                await API.cambiarPassword({
                    passwordActual: formData.get('passwordActual'),
                    passwordNueva: formData.get('passwordNueva')
                });

                Utils.closeModal();
                Utils.showToast('Contraseña actualizada exitosamente', 'success');
            } catch (error) {
                Utils.showToast(error.message || 'Error al cambiar contraseña', 'error');
            }
        });
    }
};