
Views.auth = {
    
    renderLogin() {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="max-w-md mx-auto bg-white p-8 rounded-lg shadow-lg animate-fade-in">
                <h2 class="text-3xl font-bold mb-6 text-center">Iniciar Sesión</h2>
                <form id="loginForm" class="space-y-4">
                    <div>
                        <label class="block text-gray-700 mb-2">Email</label>
                        <input type="email" name="email" required 
                            class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            placeholder="correo@ejemplo.com">
                    </div>
                    <div>
                        <label class="block text-gray-700 mb-2">Contraseña</label>
                        <input type="password" name="password" required 
                            class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            placeholder="••••••••">
                    </div>
                    <button type="submit" 
                        class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition">
                        <i class="fas fa-sign-in-alt mr-2"></i> Iniciar Sesión
                    </button>
                </form>
                <p class="mt-4 text-center text-gray-600">
                    ¿No tienes cuenta? <a href="#registro" class="text-blue-600 hover:underline font-semibold">Regístrate aquí</a>
                </p>
            </div>
        `;

        this.setupLoginForm();
    },

    /**
     * Renderiza el formulario de registro
     */
    renderRegistro() {
        const content = document.getElementById('mainContent');
        content.innerHTML = `
            <div class="max-w-md mx-auto bg-white p-8 rounded-lg shadow-lg animate-fade-in">
                <h2 class="text-3xl font-bold mb-6 text-center">Crear Cuenta</h2>
                <form id="registroForm" class="space-y-4">
                    <div class="grid md:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-gray-700 mb-2">Nombre *</label>
                            <input type="text" name="nombre" required 
                                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                        </div>
                        <div>
                            <label class="block text-gray-700 mb-2">Apellido *</label>
                            <input type="text" name="apellido" required 
                                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                        </div>
                    </div>
                    <div>
                        <label class="block text-gray-700 mb-2">Email *</label>
                        <input type="email" name="email" required 
                            class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            placeholder="correo@ejemplo.com">
                    </div>
                    <div>
                        <label class="block text-gray-700 mb-2">Contraseña *</label>
                        <input type="password" name="password" required 
                            class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            placeholder="Mínimo 8 caracteres">
                        <p class="text-sm text-gray-500 mt-1">
                            <i class="fas fa-info-circle mr-1"></i>
                            Debe incluir mayúsculas, minúsculas, números y caracteres especiales
                        </p>
                    </div>
                    <div>
                        <label class="block text-gray-700 mb-2">Confirmar Contraseña *</label>
                        <input type="password" name="confirmPassword" required 
                            class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            placeholder="Confirma tu contraseña">
                    </div>
                    <button type="submit" 
                        class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition">
                        <i class="fas fa-user-plus mr-2"></i> Registrarse
                    </button>
                </form>
                <p class="mt-4 text-center text-gray-600">
                    ¿Ya tienes cuenta? <a href="#login" class="text-blue-600 hover:underline font-semibold">Inicia sesión</a>
                </p>
            </div>
        `;

        this.setupRegistroForm();
    },

 
    setupLoginForm() {
        document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const data = {
                email: formData.get('email'),
                password: formData.get('password')
            };

            try {
                const result = await API.usuarios.login(data);
                App.currentUser = result.data;
                App.updateNavbar();
                App.navigateTo('home');
                showToast('¡Bienvenido de nuevo!', 'success');
            } catch (error) {
                showToast(error.message || 'Error al iniciar sesión', 'error');
            }
        });
    },

    setupRegistroForm() {
        const form = document.getElementById('registroForm');
        
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            
 
            const password = formData.get('password');
            const confirmPassword = formData.get('confirmPassword');
            
            if (password !== confirmPassword) {
                showToast('Las contraseñas no coinciden', 'error');
                return;
            }

            const data = {
                nombre: formData.get('nombre'),
                apellido: formData.get('apellido'),
                email: formData.get('email'),
                password: password
            };

            try {
                await API.usuarios.registrar(data);
                showToast('Cuenta creada exitosamente. Ya puedes iniciar sesión', 'success');
                App.navigateTo('login');
            } catch (error) {
                showToast(error.message || 'Error al registrar', 'error');
            }
        });
    }
};