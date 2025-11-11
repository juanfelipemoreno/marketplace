
function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    const colors = {
        success: 'bg-green-500',
        error: 'bg-red-500',
        warning: 'bg-yellow-500',
        info: 'bg-blue-500'
    };
    
    const icons = {
        success: 'check-circle',
        error: 'exclamation-circle',
        warning: 'exclamation-triangle',
        info: 'info-circle'
    };
    
    toast.className = `${colors[type]} text-white px-6 py-4 rounded-lg shadow-lg animate-fade-in`;
    toast.innerHTML = `
        <div class="flex items-center space-x-2">
            <i class="fas fa-${icons[type]}"></i>
            <span>${message}</span>
        </div>
    `;
    
    container.appendChild(toast);
    
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, CONFIG.TOAST_DURATION);
}


function showModal(title, content, size = 'max-w-2xl') {
    const container = document.getElementById('modalContainer');
    container.innerHTML = `
        <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" onclick="closeModal()">
            <div class="bg-white rounded-lg shadow-xl ${size} w-full mx-4 max-h-[90vh] overflow-y-auto animate-fade-in" onclick="event.stopPropagation()">
                <div class="flex justify-between items-center p-6 border-b sticky top-0 bg-white z-10">
                    <h3 class="text-2xl font-bold">${title}</h3>
                    <button onclick="closeModal()" class="text-gray-500 hover:text-gray-700">
                        <i class="fas fa-times text-2xl"></i>
                    </button>
                </div>
                <div class="p-6">${content}</div>
            </div>
        </div>
    `;
}

/**
 * Cierra el modal
 */
function closeModal() {
    document.getElementById('modalContainer').innerHTML = '';
}

/**
 * Muestra un loader en el contenido principal
 */
function showLoader() {
    const content = document.getElementById('mainContent');
    content.innerHTML = '<div class="flex justify-center py-20"><div class="loader"></div></div>';
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP',
        minimumFractionDigits: 0
    }).format(amount);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('es-CO', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    }).format(date);
}

function formatDateTime(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('es-CO', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(date);
}

function isValidEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function truncateText(text, maxLength) {
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}