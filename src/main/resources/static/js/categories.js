// State variables
let currentPage = 0;
let pageSize = 5;
let currentSearch = '';
let totalPages = 1;
let shippers = [];

// DOM Elements
const tableBody = document.getElementById('categoriesTableBody');
const modal = document.getElementById('categoryModal');
const categoryForm = document.getElementById('categoryForm');
const modalTitle = document.getElementById('modalTitle');
const searchInput = document.getElementById('searchInput');
const shipperFilter = document.getElementById('shipperFilter');

// Initialize on load
document.addEventListener('DOMContentLoaded', () => {
    fetchShippers();
    fetchCategories();
});

// Show Toast Notification
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;

    const icon = type === 'success' ?
        `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>` :
        `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>`;

    toast.innerHTML = `${icon} <span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Fetch Shippers
async function fetchShippers() {
    try {
        const response = await fetch('/api/organizations/shippers');
        const result = await response.json();
        if (result.success && Array.isArray(result.data)) {
            shippers = result.data;
            shippers.forEach(s => {
                const option = document.createElement('option');
                option.value = s.organizationId;
                option.textContent = s.organizationName || s.organizationId || s.id;
                shipperFilter.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Failed to fetch shippers:', error);
    }
}

function handleShipperChange() {
    currentPage = 0;
    fetchCategories();
}

// Fetch Categories
async function fetchCategories() {
    try {
        const activeShipperId = shipperFilter.value;
        let url = `/api/categories?search=${encodeURIComponent(currentSearch)}&pageNumber=${currentPage}&pageSize=${pageSize}`;
        if (activeShipperId) {
            url += `&shipperId=${activeShipperId}`;
        }

        const response = await fetch(url);
        const result = await response.json();

        if (result.success && Array.isArray(result.data)) {
            const dataList = result.data;
            renderCategories(dataList);

            document.getElementById('pageInfo').textContent = `Showing page ${currentPage + 1}`;
            document.getElementById('prevBtn').disabled = currentPage === 0;
            document.getElementById('nextBtn').disabled = dataList.length < pageSize;
        } else {
            showToast(result.message || 'Failed to fetch categories', 'error');
        }
    } catch (error) {
        showToast('Error loading categories.', 'error');
        console.error(error);
    }
}

// Render Table
function renderCategories(categories) {
    tableBody.innerHTML = '';

    if (!categories || categories.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--text-secondary);">No categories found</td></tr>`;
        return;
    }

    categories.forEach(c => {
        const tr = document.createElement('tr');

        // Find shipper name from cache
        const shipper = shippers.find(s => s.organizationId == c.shipperId);
        const shipperDisplayName = shipper ? shipper.organizationName : (c.shipperId || '-');

        // Logic for Parent vs Child columns
        let parentCell = '-';
        let childCell = '-';

        if (c.parentCategoryName) {
            parentCell = c.parentCategoryName;
            childCell = c.categoryName;
        } else {
            // It's a root category
            parentCell = c.categoryName;
            childCell = '-';
        }

        tr.innerHTML = `
            <td style="font-weight: 500;">${parentCell}</td>
            <td>${childCell}</td>
            <td>${shipperDisplayName}</td>
            <td>
                <button class="btn-icon" onclick='editCategory(${JSON.stringify(c).replace(/'/g, "&#39;")})' title="Edit">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                    </svg>
                </button>
                <button class="btn-icon" onclick="confirmDelete(${c.categoryId})" title="Delete" style="color: var(--danger-color);">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="3 6 5 6 21 6"></polyline>
                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                    </svg>
                </button>
            </td>
        `;
        tableBody.appendChild(tr);
    });
}

// Search with Debounce
let searchTimeout;
function debounceSearch() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => {
        currentSearch = searchInput.value;
        currentPage = 0;
        fetchCategories();
    }, 500);
}

// Pagination
function changePage(delta) {
    const newPage = currentPage + delta;
    if (newPage >= 0) {
        currentPage = newPage;
        fetchCategories();
    }
}

// Modal Management
function openModal() {
    const activeShipperId = shipperFilter.value;

    if (!activeShipperId || activeShipperId === '') {
        showToast('Please select a specific Shipper from the top dropdown before adding a category.', 'error');
        return;
    }

    categoryForm.reset();
    document.getElementById('categoryId').value = '';
    document.getElementById('hiddenShipperId').value = activeShipperId;

    // Show active shipper context in modal
    const shipper = shippers.find(s => s.organizationId.toString() === activeShipperId.toString());
    const shipperName = shipper ? shipper.organizationName : 'Unknown Shipper';
    document.getElementById('modalShipperContext').textContent = `Organization: ${shipperName}`;

    // Load parent categories for this shipper
    loadParentCategories(activeShipperId);

    modalTitle.textContent = 'Add New Category';
    modal.classList.add('active');
}

async function loadParentCategories(shipperId, selectedParentId = null) {
    const parentSelect = document.getElementById('parentCategoryId');
    parentSelect.innerHTML = '<option value="">-- No Parent (Root) --</option>';

    try {
        const response = await fetch(`/api/categories/shipper/${shipperId}`);
        const result = await response.json();
        if (result.success && Array.isArray(result.data)) {
            // Only show categories that don't have a parent (top-level) to avoid deep nesting for now
            // or show all. Let's show all for flexibility.
            result.data.forEach(cat => {
                const option = document.createElement('option');
                option.value = cat.categoryId;
                option.textContent = cat.categoryName;
                if (selectedParentId && selectedParentId == cat.categoryId) {
                    option.selected = true;
                }
                parentSelect.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Failed to load parent categories:', error);
    }
}

function closeModal() {
    modal.classList.remove('active');
}

// Save (Create/Update)
async function saveCategory() {
    const name = document.getElementById('categoryName').value;
    if (!name) {
        showToast('Category Name is required', 'error');
        return;
    }

    const shipperId = document.getElementById('hiddenShipperId').value;
    if (!shipperId) {
        showToast('Shipper context is missing. Please select a shipper.', 'error');
        return;
    }

    const id = document.getElementById('categoryId').value;
    const isEditing = !!id;

    const payload = {
        categoryName: name,
        parentCategoryId: document.getElementById('parentCategoryId').value ? parseInt(document.getElementById('parentCategoryId').value) : null,
        parentCategoryName: document.getElementById('parentCategoryName').value || null,
        shipperId: parseInt(shipperId)
    };

    const url = isEditing ? `/api/categories/${id}` : '/api/categories';
    const method = isEditing ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const result = await response.json();

        if (response.ok && (result.status === 'SUCCESS' || result.status === 'CREATED')) {
            showToast(result.message || 'Category saved successfully');
            closeModal();
            fetchCategories();
        } else {
            showToast(result.message || 'Failed to save category', 'error');
        }
    } catch (error) {
        showToast('An error occurred while saving', 'error');
        console.error(error);
    }
}

// Edit Category
function editCategory(c) {
    categoryForm.reset();
    document.getElementById('categoryId').value = c.categoryId;
    document.getElementById('categoryName').value = c.categoryName || '';
    document.getElementById('parentCategoryName').value = c.parentCategoryName || '';
    document.getElementById('hiddenShipperId').value = c.shipperId || '';

    // Show active shipper context in modal
    const shipper = shippers.find(s => s.organizationId == c.shipperId);
    const shipperName = shipper ? shipper.organizationName : `Shipper ${c.shipperId}`;
    document.getElementById('modalShipperContext').textContent = `Organization: ${shipperName}`;

    // Load parent categories and select the current one
    loadParentCategories(c.shipperId, c.parentCategoryId);

    modalTitle.textContent = 'Edit Category';
    modal.classList.add('active');
}

async function confirmDelete(id) {
    if (confirm('Are you sure you want to delete this category?')) {
        try {
            const response = await fetch(`/api/categories/${id}`, {
                method: 'DELETE'
            });
            const result = await response.json();
            if (response.ok && result.status === 'SUCCESS') {
                showToast(result.message || 'Category deleted successfully');
                fetchCategories();
            } else {
                showToast(result.message || 'Failed to delete category', 'error');
            }
        } catch (error) {
            showToast('Error deleting category', 'error');
            console.error(error);
        }
    }
}
