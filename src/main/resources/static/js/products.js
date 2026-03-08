// State variables - v1.0.3 (Fixed Shipper Validation)
let currentPage = 0;
let pageSize = 5;
let currentSearch = '';
let totalPages = 1;

let categories = []; // Cache categories
let shippers = []; // Cache shippers

// DOM Elements
const tableBody = document.getElementById('productsTableBody');
const modal = document.getElementById('productModal');
const productForm = document.getElementById('productForm');
const modalTitle = document.getElementById('modalTitle');
const searchInput = document.getElementById('searchInput');
const shipperFilter = document.getElementById('shipperFilter');

// Initialize on load
document.addEventListener('DOMContentLoaded', () => {
    fetchShippers();
    fetchCategories();
    fetchProducts();
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

// Fetch Shippers for Dropdown Filter
async function fetchShippers() {
    try {
        const response = await fetch('/api/organizations/shippers');
        const result = await response.json();
        if (result.success && Array.isArray(result.data)) {
            shippers = result.data;
            shippers.forEach(s => {
                const option = document.createElement('option');
                option.value = s.organizationId || s.id;
                option.textContent = s.organizationName || s.organizationId || s.id;
                shipperFilter.appendChild(option);
            });
        } else {
            console.error("Failed to parse shippers:", result);
        }
    } catch (error) {
        console.error('Failed to fetch shippers:', error);
    }
}

function handleShipperChange() {
    console.log("[DEBUG] Shipper changed to:", shipperFilter.value);
    currentPage = 0; // Reset pagination
    currentSearch = searchInput.value; // Keep current search text
    fetchProducts();
}

// Fetch Categories for Hierarchical Dropdown
async function fetchCategories() {
    try {
        const response = await fetch('/api/categories/load-all');
        const data = await response.json();

        // The API returns success: false but provides the full array in data
        if (Array.isArray(data.data) && data.data.length > 0) {
            categories = data.data; // Cache flat list for table rendering

            // Build hierarchy for dropdown
            const select = document.getElementById('categoryId');
            select.innerHTML = '<option value="">Select Category</option>';

            // Separate parents and children
            const parents = categories.filter(c => !c.parentCategoryId);
            const children = categories.filter(c => c.parentCategoryId);

            parents.forEach(parent => {
                const optgroup = document.createElement('optgroup');
                optgroup.label = parent.categoryName || `Category ${parent.categoryId}`;

                // Find subcategories for this parent
                const subcats = children.filter(c => c.parentCategoryId === parent.categoryId);
                subcats.forEach(sub => {
                    const option = document.createElement('option');
                    option.value = sub.categoryId;
                    option.textContent = sub.categoryName || `Subcategory ${sub.categoryId}`;
                    optgroup.appendChild(option);
                });

                select.appendChild(optgroup);
            });

            // Handle any children that might have orphaned parent IDs (just in case)
            const orphaned = children.filter(c => !parents.some(p => p.categoryId === c.parentCategoryId));
            if (orphaned.length > 0) {
                const otherGroup = document.createElement('optgroup');
                otherGroup.label = "Other Categories";
                orphaned.forEach(sub => {
                    const option = document.createElement('option');
                    option.value = sub.categoryId;
                    option.textContent = sub.categoryName || `Subcategory ${sub.categoryId}`;
                    otherGroup.appendChild(option);
                });
                select.appendChild(otherGroup);
            }
        }
    } catch (error) {
        console.error('Failed to fetch categories:', error);
    }
}

function getCategoryName(id) {
    if (!id) return '-';
    const cat = categories.find(c => c.categoryId === id);
    return cat ? cat.categoryName : id;
}

// Fetch Products (Handles both All paginated, and Shipper-specific list)
async function fetchProducts() {
    // Determine the current shipper filter, handling string cases correctly
    const shipperIdVal = shipperFilter.value;
    const shipperId = (shipperIdVal && shipperIdVal !== '') ? parseInt(shipperIdVal) : null;

    try {
        let apiUrl = '';
        let isPaginatedResponse = false;

        if (shipperId) {
            // Fetch by specific shipper - List without pagination from server
            apiUrl = `/api/products/shipper/${shipperId}`;
        } else {
            // Fetch all with server-side pagination
            apiUrl = `/api/products?search=${encodeURIComponent(currentSearch)}&pageNumber=${currentPage}&size=${pageSize}`;
            isPaginatedResponse = true; // Signals we expect data.content instead of Array
        }

        const response = await fetch(apiUrl);
        const result = await response.json();

        if (result.success) {
            let productsArray = [];

            if (isPaginatedResponse) {
                // Handle PageResponse structure
                productsArray = result.data.content;
                totalPages = result.data.totalPages || 1;

                document.getElementById('pageInfo').textContent = `Showing page ${currentPage + 1} of ${totalPages}`;
                document.getElementById('prevBtn').disabled = currentPage === 0;
                document.getElementById('nextBtn').disabled = currentPage >= totalPages - 1;
            } else {
                // Handle ListResponse structure (Shipper specific)
                productsArray = result.data || [];

                // Client-side filtering and pagination simulation for Shipper List
                // 1. Filter by search term
                if (currentSearch) {
                    const searchLower = currentSearch.toLowerCase();
                    productsArray = productsArray.filter(p =>
                        (p.productName && p.productName.toLowerCase().includes(searchLower)) ||
                        (p.productUniqueId && p.productUniqueId.toLowerCase().includes(searchLower))
                    );
                }

                // 2. Paginate Array
                totalPages = Math.ceil(productsArray.length / pageSize) || 1;
                if (currentPage >= totalPages) currentPage = Math.max(0, totalPages - 1);

                const startIndex = currentPage * pageSize;
                productsArray = productsArray.slice(startIndex, startIndex + pageSize);

                document.getElementById('pageInfo').textContent = `Showing page ${currentPage + 1} of ${totalPages} (Total: ${result.data.length || 0})`;
                document.getElementById('prevBtn').disabled = currentPage === 0;
                document.getElementById('nextBtn').disabled = currentPage >= totalPages - 1;
            }

            renderProducts(productsArray);

        } else {
            showToast(result.message || 'Failed to fetch products', 'error');
        }
    } catch (error) {
        showToast('Error loading products. Is the server running?', 'error');
        console.error(error);
    }
}

// Render Table
function renderProducts(products) {
    tableBody.innerHTML = '';

    if (!products || products.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--text-secondary);">No products found</td></tr>`;
        return;
    }

    products.forEach(p => {
        const tr = document.createElement('tr');

        const dims = p.length ? `${p.length}x${p.breadth}x${p.height} ${p.dimensionUom || ''}` : '-';
        const weight = p.weight ? `${p.weight} ${p.weightUom || ''}` : '-';

        // Extract category ID (can be in directly p.categoryId or nested object)
        const parsedCategoryId = (typeof p.category === 'object' && p.category !== null) ? p.category.categoryId : p.categoryId;
        const mappedCategoryName = getCategoryName(parsedCategoryId);

        // Determine Serializable symbol
        const serializableIcon = p.serializable ?
            `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--secondary-color)" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>` :
            `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--danger-color)" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>`;

        tr.innerHTML = `
            <td style="font-family: monospace; color: var(--text-secondary);">${p.productUniqueId || '-'}</td>
            <td style="font-weight: 500;">${p.productName || '-'}</td>
            <td>${mappedCategoryName}</td>
            <td><div style="font-size: 0.85rem; max-width: 250px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="${p.description || ''}">${p.description || '-'}</div></td>
            <td>${dims}</td>
            <td>${weight}</td>
            <td style="text-align: center;">${serializableIcon}</td>
            <td>
                <button class="btn-icon" onclick="editProduct(${p.productId})" title="Edit">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                    </svg>
                </button>
                <button class="btn-icon" onclick="confirmDelete(${p.productId})" title="Delete" style="color: var(--danger-color);">
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
        currentPage = 0; // Reset to page 0
        fetchProducts();
    }, 500);
}

// Pagination
function changePage(delta) {
    const newPage = currentPage + delta;
    if (newPage >= 0 && newPage < totalPages) {
        currentPage = newPage;
        fetchProducts();
    }
}

// Modal Management
function openModal() {
    const activeShipperId = shipperFilter.value;

    if (!activeShipperId || activeShipperId === '' || activeShipperId === 'null' || activeShipperId === 'undefined') {
        showToast('Please select a specific Shipper from the top dropdown.', 'error');
        return;
    }

    productForm.reset();
    document.getElementById('productId').value = '';

    // Set hidden shipper ID
    document.getElementById('hiddenShipperId').value = activeShipperId;

    // Show active shipper context in modal
    const shipper = shippers.find(s => s.organizationId && s.organizationId.toString() === activeShipperId.toString());
    const shipperName = shipper ? (shipper.organizationName || shipper.organizationId) : 'Unknown Shipper';
    document.getElementById('modalShipperContext').textContent = `Organization: ${shipperName}`;


    modalTitle.textContent = 'Add New Product';

    // Set default UOMs
    document.getElementById('dimensionUom').value = 'cm';
    document.getElementById('weightUom').value = 'kg';
    modal.classList.add('active');
}

function closeModal() {
    modal.classList.remove('active');
}

// Save (Create/Update)
async function saveProduct() {
    // Basic validation
    const name = document.getElementById('productName').value;
    if (!name) {
        showToast('Product Name is required', 'error');
        return;
    }

    const shipperIdRaw = document.getElementById('hiddenShipperId').value;
    console.log("Saving product. hiddenShipperId:", shipperIdRaw);

    if (!shipperIdRaw || shipperIdRaw === '' || shipperIdRaw === 'null' || shipperIdRaw === 'undefined') {
        showToast('Save failed: Shipper organization context is missing. Please select a shipper and try again.', 'error');
        console.error("Save failed: hiddenShipperId missing", { shipperIdRaw });
        return;
    }
    const shipperId = parseInt(shipperIdRaw);
    if (isNaN(shipperId)) {
        showToast('Save failed: Invalid Shipper ID detected.', 'error');
        return;
    }

    const id = document.getElementById('productId').value;
    const isEditing = !!id;

    const payload = {
        productName: name,
        productUniqueId: document.getElementById('productUniqueId').value,
        categoryId: document.getElementById('categoryId').value ? parseInt(document.getElementById('categoryId').value) : null,
        shipperId: shipperId,
        length: document.getElementById('length').value ? parseFloat(document.getElementById('length').value) : null,
        breadth: document.getElementById('breadth').value ? parseFloat(document.getElementById('breadth').value) : null,
        height: document.getElementById('height').value ? parseFloat(document.getElementById('height').value) : null,
        dimensionUom: document.getElementById('dimensionUom').value,
        weight: document.getElementById('weight').value ? parseFloat(document.getElementById('weight').value) : null,
        weightUom: document.getElementById('weightUom').value,
        description: document.getElementById('description').value,
        serializable: document.getElementById('serializable').checked,
        uploadImage: null // Assuming handled separately or null for now
    };

    const url = isEditing ? `/api/products/${id}` : '/api/products';
    const method = isEditing ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const result = await response.json();

        if (response.ok && result.success) {
            showToast(result.message || 'Product saved successfully');
            closeModal();
            fetchProducts();
        } else {
            showToast(result.message || 'Failed to save product', 'error');
        }
    } catch (error) {
        showToast('An error occurred while saving', 'error');
        console.error(error);
    }
}

// Edit Product (Fetch Data and Populate form)
async function editProduct(id) {
    try {
        const response = await fetch(`/api/products/${id}`);
        const result = await response.json();

        if (result.success && result.data) {
            const p = result.data;
            document.getElementById('productId').value = p.productId;
            document.getElementById('hiddenShipperId').value = p.shipperId;
            document.getElementById('productName').value = p.productName || '';
            document.getElementById('productUniqueId').value = p.productUniqueId || '';
            const parsedCategoryId = (typeof p.category === 'object' && p.category !== null) ? p.category.categoryId : p.categoryId;
            document.getElementById('categoryId').value = parsedCategoryId || '';
            document.getElementById('length').value = p.length || '';
            document.getElementById('breadth').value = p.breadth || '';
            document.getElementById('height').value = p.height || '';
            document.getElementById('dimensionUom').value = p.dimensionUom || 'cm';
            document.getElementById('weight').value = p.weight || '';
            document.getElementById('weightUom').value = p.weightUom || 'kg';
            document.getElementById('description').value = p.description || '';
            document.getElementById('serializable').checked = !!p.serializable;

            // Show active shipper context in modal
            const shipper = shippers.find(s => s.organizationId && s.organizationId.toString() === p.shipperId.toString());
            const shipperName = shipper ? (shipper.organizationName || shipper.organizationId) : `Shipper ${p.shipperId}`;
            document.getElementById('modalShipperContext').textContent = `Organization: ${shipperName}`;

            modalTitle.textContent = 'Edit Product';
            modal.classList.add('active');
        } else {
            showToast('Failed to load product details', 'error');
        }
    } catch (error) {
        showToast('Error loading product details', 'error');
        console.error(error);
    }
}

// Delete Product
async function confirmDelete(id) {
    if (confirm('Are you sure you want to delete this product? This action cannot be undone.')) {
        try {
            const response = await fetch(`/api/products/${id}`, {
                method: 'DELETE'
            });

            const result = await response.json();

            if (response.ok && result.success) {
                showToast(result.message || 'Product deleted successfully');
                // Adjust page if we deleted the last item manually
                if (tableBody.children.length === 1 && currentPage > 0) {
                    currentPage--;
                }
                fetchProducts();
            } else {
                showToast(result.message || 'Failed to delete product', 'error');
            }
        } catch (error) {
            showToast('Error deleting product', 'error');
            console.error(error);
        }
    }
}
