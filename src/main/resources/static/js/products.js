// ===================================================
// products.js  –  OMS Product Management
// ===================================================

// ------------------- State -------------------------
let currentPage   = 0;
let pageSize      = 10;
let currentSearch = '';
let totalPages    = 1;
let categories    = [];
let shippers      = [];
let logCurrentPage = 0;
let logPageSize    = 5;
let logTotalPages  = 1;

// ------------------- DOM refs ----------------------
const tableBody    = document.getElementById('productsTableBody');
const modal        = document.getElementById('productModal');
const productForm  = document.getElementById('productForm');
const modalTitle   = document.getElementById('modalTitle');
const searchInput  = document.getElementById('searchInput');
const shipperFilter = document.getElementById('shipperFilter');

// ------------------- Init --------------------------
document.addEventListener('DOMContentLoaded', () => {
    fetchShippers();
    fetchCategories();
    fetchProducts();
});

// ------------------- Toast -------------------------
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    const icon = type === 'success'
        ? `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>`
        : `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>`;
    toast.innerHTML = `${icon} <span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// ------------------- Shippers ----------------------
async function fetchShippers() {
    try {
        const res    = await fetch('/api/organizations/shippers');
        const result = await res.json();
        if (result.success && Array.isArray(result.data)) {
            shippers = result.data;
            const importSel  = document.getElementById('importShipperId');
            const historySel = document.getElementById('historyShipperFilter');
            shippers.forEach(s => {
                const val  = s.organizationId || s.id;
                const text = s.organizationName || val;
                shipperFilter.appendChild(new Option(text, val));
                if (importSel)  importSel.appendChild(new Option(text, val));
                if (historySel) historySel.appendChild(new Option(text, val));
            });
        }
    } catch (e) {
        console.error('fetchShippers error', e);
    }
}

function handleShipperChange() {
    currentPage   = 0;
    currentSearch = searchInput.value;
    fetchProducts();
}

// ------------------- Categories --------------------
async function fetchCategories() {
    try {
        const res  = await fetch('/api/categories/load-all');
        const data = await res.json();
        if (Array.isArray(data.data) && data.data.length > 0) {
            categories = data.data;
            const select = document.getElementById('categoryId');
            select.innerHTML = '<option value="">Select Category</option>';
            const parents  = categories.filter(c => !c.parentCategoryId);
            const children = categories.filter(c =>  c.parentCategoryId);
            parents.forEach(parent => {
                const grp = document.createElement('optgroup');
                grp.label = parent.categoryName || `Category ${parent.categoryId}`;
                children.filter(c => c.parentCategoryId === parent.categoryId).forEach(sub => {
                    grp.appendChild(new Option(sub.categoryName || `Sub ${sub.categoryId}`, sub.categoryId));
                });
                select.appendChild(grp);
            });
        }
    } catch (e) {
        console.error('fetchCategories error', e);
    }
}

function getCategoryName(id) {
    if (!id) return '-';
    const cat = categories.find(c => c.categoryId === id);
    return cat ? cat.categoryName : id;
}

// ------------------- Products ----------------------
async function fetchProducts() {
    const shipperIdVal = shipperFilter.value;
    const shipperId    = (shipperIdVal && shipperIdVal !== '') ? parseInt(shipperIdVal) : null;
    try {
        let url, paginated = false;
        if (shipperId) {
            url = `/api/products/shipper/${shipperId}`;
        } else {
            url = `/api/products?search=${encodeURIComponent(currentSearch)}&pageNumber=${currentPage}&size=${pageSize}`;
            paginated = true;
        }
        const res    = await fetch(url);
        const result = await res.json();
        if (!result.success) { showToast(result.message || 'Failed to fetch products', 'error'); return; }

        let list = [];
        if (paginated) {
            list = result.data.content || [];
            totalPages = result.data.totalPages || 1;
        } else {
            list = result.data || [];
            if (currentSearch) {
                const q = currentSearch.toLowerCase();
                list = list.filter(p =>
                    (p.productName      && p.productName.toLowerCase().includes(q)) ||
                    (p.productUniqueId  && p.productUniqueId.toLowerCase().includes(q))
                );
            }
            totalPages = Math.ceil(list.length / pageSize) || 1;
            if (currentPage >= totalPages) currentPage = Math.max(0, totalPages - 1);
            const s = currentPage * pageSize;
            list = list.slice(s, s + pageSize);
        }

        document.getElementById('pageInfo').textContent = `Page ${currentPage + 1} of ${totalPages}`;
        document.getElementById('prevBtn').disabled = currentPage === 0;
        document.getElementById('nextBtn').disabled = currentPage >= totalPages - 1;
        renderProducts(list);
    } catch (e) {
        showToast('Error loading products. Is the server running?', 'error');
        console.error(e);
    }
}

function renderProducts(products) {
    tableBody.innerHTML = '';
    if (!products || products.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="9" style="text-align:center;color:var(--text-secondary);padding:2rem;">No products found</td></tr>`;
        return;
    }
    products.forEach(p => {
        const tr = document.createElement('tr');
        const parsedCatId = (typeof p.category === 'object' && p.category !== null) ? p.category.categoryId : p.categoryId;
        const dims  = p.length ? `${p.length}×${p.breadth}×${p.height} ${p.dimensionUom || ''}` : '-';
        const wt    = p.weight ? `${p.weight} ${p.weightUom || ''}` : '-';
        const serIcon = p.serializable
            ? `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--secondary-color)" stroke-width="3"><polyline points="20 6 9 17 4 12"></polyline></svg>`
            : `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="var(--danger-color)" stroke-width="3"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>`;
        const imgThumb = p.uploadImage
            ? `<img src="${p.uploadImage}" alt="img" style="width:36px;height:36px;object-fit:cover;border-radius:6px;cursor:pointer;" onclick="showImagePreview('${p.uploadImage}')">`
            : `<span style="color:var(--text-secondary);font-size:0.75rem;">—</span>`;
        tr.innerHTML = `
            <td style="font-family:monospace;color:var(--text-secondary);">${p.productUniqueId || '-'}</td>
            <td style="font-weight:500;">${p.productName || '-'}</td>
            <td>${getCategoryName(parsedCatId)}</td>
            <td><div style="max-width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:0.85rem;" title="${p.description || ''}">${p.description || '-'}</div></td>
            <td>${dims}</td>
            <td>${wt}</td>
            <td>${p.availableQty != null ? p.availableQty : '-'}</td>
            <td style="text-align:center;">${serIcon}</td>
            <td style="text-align:center;">${imgThumb}</td>
            <td>
                <button class="btn-icon" onclick="editProduct(${p.productId})" title="Edit">
                    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                </button>
                <button class="btn-icon" onclick="confirmDelete(${p.productId})" title="Delete" style="color:var(--danger-color);">
                    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
                </button>
            </td>`;
        tableBody.appendChild(tr);
    });
}

// ------------------- Search / Pagination -----------
let searchTimeout;
function debounceSearch() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => {
        currentSearch = searchInput.value;
        currentPage   = 0;
        fetchProducts();
    }, 500);
}

function changePage(delta) {
    const next = currentPage + delta;
    if (next >= 0 && next < totalPages) { currentPage = next; fetchProducts(); }
}

// ------------------- Product Modal -----------------
function openModal() {
    const sid = shipperFilter.value;
    if (!sid || sid === '' || sid === 'null') {
        showToast('Please select a specific Shipper first.', 'error');
        return;
    }
    productForm.reset();
    document.getElementById('productId').value       = '';
    document.getElementById('hiddenShipperId').value = sid;
    document.getElementById('dimensionUom').value    = 'cm';
    document.getElementById('weightUom').value       = 'kg';
    clearImagePreview();
    const shipper = shippers.find(s => (s.organizationId || s.id).toString() === sid.toString());
    document.getElementById('modalShipperContext').textContent = `Organization: ${shipper ? (shipper.organizationName || shipper.organizationId) : 'Unknown'}`;
    modalTitle.textContent = 'Add New Product';
    modal.classList.add('active');
}

function closeModal() {
    modal.classList.remove('active');
}

// ------------------- Image Preview in Form ---------
function previewProductImage(input) {
    const preview  = document.getElementById('productImagePreview');
    const placeholder = document.getElementById('imgPlaceholder');
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = e => {
            preview.src = e.target.result;
            preview.style.display = 'block';
            if (placeholder) placeholder.style.display = 'none';
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function clearImagePreview() {
    const preview = document.getElementById('productImagePreview');
    const placeholder = document.getElementById('imgPlaceholder');
    const input   = document.getElementById('productImage');
    if (preview)  { preview.src = ''; preview.style.display = 'none'; }
    if (placeholder) placeholder.style.display = 'flex';
    if (input)    input.value = '';
}

// Light-box for table image thumbnails
function showImagePreview(url) {
    const overlay = document.getElementById('imagePreviewOverlay');
    document.getElementById('imagePreviewImg').src = url;
    overlay.style.display = 'flex';
}
function closeImagePreview() {
    const overlay = document.getElementById('imagePreviewOverlay');
    overlay.style.display = 'none';
}

// ------------------- Save Product ------------------
async function saveProduct() {
    const name = document.getElementById('productName').value;
    if (!name) { showToast('Product Name is required', 'error'); return; }

    const sidRaw = document.getElementById('hiddenShipperId').value;
    if (!sidRaw || sidRaw === '' || sidRaw === 'null') {
        showToast('Shipper context missing – please re-open the form.', 'error'); return;
    }
    const shipperId = parseInt(sidRaw);
    if (isNaN(shipperId)) { showToast('Invalid Shipper ID.', 'error'); return; }

    const id        = document.getElementById('productId').value;
    const isEditing = !!id;
    const imgInput  = document.getElementById('productImage');

    const catId   = document.getElementById('categoryId').value;
    const catName  = catId ? (categories.find(c => c.categoryId == catId) || {}).categoryName || null : null;

    const preview = document.getElementById('productImagePreview');
    let currentImageUrl = null;
    // If preview is visible and its src is a real URL (not a data: string), it's the current stored image
    if (preview && preview.style.display !== 'none' && preview.src && !preview.src.startsWith('data:')) {
        currentImageUrl = preview.src;
    }

    const productPayload = {
        productName:      name,
        productUniqueId:  document.getElementById('productUniqueId').value,
        categoryName:     catName,
        shipperId,
        length:           document.getElementById('length').value      ? parseFloat(document.getElementById('length').value)  : null,
        breadth:          document.getElementById('breadth').value     ? parseFloat(document.getElementById('breadth').value) : null,
        height:           document.getElementById('height').value      ? parseFloat(document.getElementById('height').value)  : null,
        dimensionUom:     document.getElementById('dimensionUom').value,
        weight:           document.getElementById('weight').value      ? parseFloat(document.getElementById('weight').value)  : null,
        weightUom:        document.getElementById('weightUom').value,
        description:      document.getElementById('description').value,
        serializable:     document.getElementById('serializable').checked,
        uploadImage:      currentImageUrl
    };

    const fd = new FormData();
    fd.append('product', new Blob([JSON.stringify(productPayload)], { type: 'application/json' }));
    if (imgInput && imgInput.files && imgInput.files[0]) {
        fd.append('image', imgInput.files[0]);
    }

    try {
        let res;
        if (isEditing) {
            res = await fetch(`/api/products/${id}`, {
                method: 'PUT',
                body: fd
            });
        } else {
            res = await fetch('/api/products', { 
                method: 'POST', 
                body: fd 
            });
        }

        const result = await res.json();
        if (res.ok && result.success) {
            showToast(result.message || 'Product saved successfully');
            closeModal();
            fetchProducts();
        } else {
            showToast(result.message || 'Failed to save product', 'error');
        }
    } catch (e) {
        showToast('An error occurred while saving', 'error');
        console.error(e);
    }
}

// ------------------- Edit Product ------------------
async function editProduct(id) {
    try {
        const res    = await fetch(`/api/products/${id}`);
        const result = await res.json();
        if (result.success && result.data) {
            const p = result.data;
            document.getElementById('productId').value            = p.productId;
            document.getElementById('hiddenShipperId').value      = p.shipperId;
            document.getElementById('productName').value          = p.productName || '';
            document.getElementById('productUniqueId').value      = p.productUniqueId || '';
            const parsedCatId = (typeof p.category === 'object' && p.category !== null) ? p.category.categoryId : p.categoryId;
            document.getElementById('categoryId').value           = parsedCatId || '';
            document.getElementById('length').value               = p.length || '';
            document.getElementById('breadth').value              = p.breadth || '';
            document.getElementById('height').value               = p.height || '';
            document.getElementById('dimensionUom').value         = p.dimensionUom || 'cm';
            document.getElementById('weight').value               = p.weight || '';
            document.getElementById('weightUom').value            = p.weightUom || 'kg';
            document.getElementById('description').value          = p.description || '';
            document.getElementById('serializable').checked       = !!p.serializable;

            // Image preview if exists
            const preview = document.getElementById('productImagePreview');
            const placeholder = document.getElementById('imgPlaceholder');
            if (p.uploadImage) {
                preview.src = p.uploadImage;
                preview.style.display = 'block';
                if (placeholder) placeholder.style.display = 'none';
            } else {
                clearImagePreview();
            }

            const shipper = shippers.find(s => (s.organizationId || s.id).toString() === p.shipperId.toString());
            document.getElementById('modalShipperContext').textContent = `Organization: ${shipper ? (shipper.organizationName || shipper.organizationId) : `Shipper ${p.shipperId}`}`;
            modalTitle.textContent = 'Edit Product';
            modal.classList.add('active');
        } else {
            showToast('Failed to load product details', 'error');
        }
    } catch (e) {
        showToast('Error loading product details', 'error');
        console.error(e);
    }
}

// ------------------- Delete Product ----------------
async function confirmDelete(id) {
    if (!confirm('Delete this product? This cannot be undone.')) return;
    try {
        const res    = await fetch(`/api/products/${id}`, { method: 'DELETE' });
        const result = await res.json();
        if (res.ok && result.success) {
            showToast(result.message || 'Product deleted');
            if (tableBody.children.length === 1 && currentPage > 0) currentPage--;
            fetchProducts();
        } else {
            showToast(result.message || 'Failed to delete product', 'error');
        }
    } catch (e) {
        showToast('Error deleting product', 'error');
        console.error(e);
    }
}

// ------------------- Export ------------------------
async function exportProducts() {
    window.location.href = '/api/products/export';
}

// ------------------- Import Modal ------------------
function openImportModal() {
    document.getElementById('importFile').value = '';
    document.getElementById('fileNameDisplay').textContent = '';
    document.getElementById('importShipperId').value = shipperFilter.value || '';
    switchImportTab('uploadTab', document.querySelector('#importModal .tab-btn'));
    document.getElementById('importModal').classList.add('active');
}

function closeImportModal() {
    document.getElementById('importModal').classList.remove('active');
}

function switchImportTab(tabId, tabEl) {
    document.querySelectorAll('#importModal .tab-btn').forEach(b => b.classList.remove('active'));
    document.querySelectorAll('#importModal .tab-content').forEach(c => c.classList.remove('active'));
    tabEl.classList.add('active');
    document.getElementById(tabId).classList.add('active');
    if (tabId === 'historyTab') {
        const hf = document.getElementById('historyShipperFilter');
        if (!hf.value) hf.value = document.getElementById('importShipperId').value;
        if (hf.value) { logCurrentPage = 0; fetchFileLogs(); }
    }
}

// --- Drag-and-drop ---
const dropzone = document.getElementById('dropzone');
if (dropzone) {
    const prevent = e => { e.preventDefault(); e.stopPropagation(); };
    ['dragenter','dragover','dragleave','drop'].forEach(ev => dropzone.addEventListener(ev, prevent, false));
    ['dragenter','dragover'].forEach(ev  => dropzone.addEventListener(ev, () => dropzone.classList.add('dragover'),    false));
    ['dragleave','drop'].forEach(ev      => dropzone.addEventListener(ev, () => dropzone.classList.remove('dragover'), false));
    dropzone.addEventListener('drop', e => {
        const files = e.dataTransfer.files;
        if (files && files.length) {
            document.getElementById('importFile').files = files;
            updateFileName();
        }
    }, false);
}

function updateFileName() {
    const input   = document.getElementById('importFile');
    const display = document.getElementById('fileNameDisplay');
    if (input.files && input.files.length > 0) {
        display.innerHTML = `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="vertical-align:middle;margin-right:4px;"><path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"></path><polyline points="13 2 13 9 20 9"></polyline></svg> ${input.files[0].name}`;
    } else {
        display.textContent = '';
    }
}

// ------------------- Sample Download ---------------
function downloadSampleFile(type = 'csv') {
    const sampleData = "productName,productUniqueId,categoryName,length,breadth,height,dimensionUom,weight,weightUom,description,availableQuantity,serializable\nSample Product,UN123,Electronics,10.0,5.0,2.0,cm,1.5,kg,A sample product description,100,true";
    const mimeType   = type === 'csv' ? 'text/csv' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
    const blob = new Blob([sampleData], { type: mimeType });
    const aEl  = document.createElement('a');
    aEl.hidden = true;
    aEl.href   = URL.createObjectURL(blob);
    aEl.download = `sample_products_import.${type}`;
    document.body.appendChild(aEl);
    aEl.click();
    document.body.removeChild(aEl);
}

// ------------------- Upload Excel ------------------
async function uploadExcel() {
    const fileInput = document.getElementById('importFile');
    const shipperId = document.getElementById('importShipperId').value;
    if (!shipperId)            { showToast('Please select a Shipper first', 'error'); return; }
    if (!fileInput.files.length) { showToast('Please select a file', 'error'); return; }

    const fd = new FormData();
    fd.append('file', fileInput.files[0]);
    fd.append('organizationId', shipperId);

    showToast('Upload initiated. Switching to History…', 'success');
    document.getElementById('historyShipperFilter').value = shipperId;
    switchImportTab('historyTab', document.querySelectorAll('#importModal .tab-btn')[1]);

    try {
        const res    = await fetch('/api/products/import/async', { method: 'POST', body: fd });
        const result = await res.json();
        if (res.ok) {
            fileInput.value = '';
            updateFileName();
            logCurrentPage = 0;
            fetchFileLogs();
        } else {
            showToast(result?.message || 'Upload failed', 'error');
        }
    } catch (e) {
        showToast('Error connecting to upload endpoint', 'error');
        console.error(e);
    }
}

// ------------------- File Logs ---------------------
async function fetchFileLogs() {
    const shipperId = document.getElementById('historyShipperFilter').value;
    const tbody     = document.getElementById('fileLogsTableBody');
    if (!shipperId) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:var(--text-secondary);padding:2rem;">Select a shipper to view history</td></tr>';
        document.getElementById('logPrevBtn').disabled = true;
        document.getElementById('logNextBtn').disabled = true;
        return;
    }
    try {
        const res    = await fetch(`/api/products/file-logs/${shipperId}?pageNumber=${logCurrentPage}&size=${logPageSize}`);
        const result = await res.json();
        if (result.success && result.data && result.data.logResponse) {
            logTotalPages = result.data.metaData ? result.data.metaData.pageCount : 1;
            renderFileLogs(result.data.logResponse);
            renderLogPagination();
        } else {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:var(--text-secondary);padding:2rem;">No logs found</td></tr>';
            document.getElementById('logPageNav').innerHTML = '';
        }
    } catch (e) {
        showToast('Failed to fetch file logs', 'error');
        console.error(e);
    }
}

function renderFileLogs(logs) {
    const tbody = document.getElementById('fileLogsTableBody');
    tbody.innerHTML = '';
    if (!logs || logs.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:var(--text-secondary);padding:2rem;">No history found</td></tr>';
        return;
    }
    logs.forEach((log, i) => {
        const chevron = `<svg class="expand-icon" id="icon-${i}" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="transition:transform 0.2s;cursor:pointer;" onclick="toggleRow(${i})"><polyline points="9 18 15 12 9 6"></polyline></svg>`;

        // File download icon next to file name
        const fileDownload = log.fileUrl
            ? `<a href="${log.fileUrl}" download title="Download file" style="color:var(--primary-color);vertical-align:middle;margin-left:6px;">
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
              </a>`
            : '';

        const statusBadge = log.fileUploadStatus === 'PROCESSING'
            ? `<span style="font-size:0.72rem;background:rgba(255,193,7,0.15);color:#ffc107;padding:1px 6px;border-radius:10px;margin-left:6px;">Processing…</span>`
            : '';

        const tr = document.createElement('tr');
        tr.style.cursor = 'pointer';
        tr.onclick = e => { if (e.target.tagName !== 'A') toggleRow(i); };
        tr.innerHTML = `
            <td style="text-align:center;">${chevron}</td>
            <td style="font-weight:500;">${log.fileName || 'Unknown'}${fileDownload}${statusBadge}</td>
            <td>${log.shipperOrganization || '-'}</td>
            <td style="color:var(--secondary-color);font-weight:${log.successCount > 0 ? 600 : 'normal'}">${log.successCount || 0}</td>
            <td style="color:${log.failedCount > 0 ? 'var(--danger-color)' : 'inherit'};font-weight:${log.failedCount > 0 ? 600 : 'normal'};">${log.failedCount || 0}</td>
            <td style="color:var(--text-secondary);">${log.createdAt ? log.createdAt.replace('T',' ').substring(0,19) : '-'}</td>`;
        tbody.appendChild(tr);

        // Expandable detail row — present for every log entry
        const ntr = document.createElement('tr');
        ntr.id = `nested-${i}`;
        ntr.className = 'history-nested-row';
        ntr.style.display = 'none';

        // Build success list
        let successRows = '';
        try {
            let sd = log.successData;
            if (typeof sd === 'string') sd = JSON.parse(sd);
            if (Array.isArray(sd) && sd.length > 0) {
                sd.forEach(s => {
                    successRows += `<div style="font-size:0.82rem;padding:2px 0;color:var(--text-secondary);">• ${s.productName || s.productUniqueId || JSON.stringify(s)}</div>`;
                });
            } else {
                successRows = `<span style="font-size:0.82rem;color:var(--text-secondary);">No Successful Records Found</span>`;
            }
        } catch (_) {
            successRows = `<span style="font-size:0.82rem;color:var(--text-secondary);">No Successful Records Found</span>`;
        }

        // Build failed table rows
        let failRows = '';
        try {
            let fd = log.failedData;
            if (typeof fd === 'string') fd = JSON.parse(fd);
            if (Array.isArray(fd) && fd.length > 0) {
                fd.forEach(err => {
                    failRows += `<tr>
                        <td style="padding:0.4rem 0.8rem;border-bottom:1px solid var(--border-color);font-size:0.82rem;">${err.rowNumber || '-'}</td>
                        <td style="padding:0.4rem 0.8rem;border-bottom:1px solid var(--border-color);font-size:0.82rem;">${err.productName || err.productUniqueId || '-'}</td>
                        <td style="padding:0.4rem 0.8rem;border-bottom:1px solid var(--border-color);font-size:0.82rem;color:var(--danger-color);">${err.errorMessage || err.error || 'Unknown error'}</td>
                    </tr>`;
                });
            } else {
                failRows = `<tr><td colspan="3" style="padding:0.5rem 0.8rem;font-size:0.82rem;color:var(--text-secondary);">No Failed Records Found</td></tr>`;
            }
        } catch (_) {
            failRows = `<tr><td colspan="3" style="padding:0.4rem 0.8rem;font-size:0.82rem;color:var(--text-secondary);">Error parsing failure details.</td></tr>`;
        }

        ntr.innerHTML = `<td colspan="6"><div style="padding:0.75rem 1rem;background:rgba(255,255,255,0.02);border-top:1px solid var(--border-color);">
            <p style="margin:0 0 0.3rem;font-size:0.85rem;font-weight:600;">Successfully Uploaded:</p>
            <div style="margin-bottom:0.75rem;padding-left:0.5rem;">${successRows}</div>
            <p style="margin:0 0 0.3rem;font-size:0.85rem;font-weight:600;">Failed Uploaded:</p>
            <table style="width:100%;border:1px solid var(--border-color);border-radius:6px;overflow:hidden;">
                <thead style="background:rgba(255,255,255,0.05);">
                    <tr>
                        <th style="padding:0.4rem 0.8rem;font-size:0.78rem;border-bottom:1px solid var(--border-color);">Row Number</th>
                        <th style="padding:0.4rem 0.8rem;font-size:0.78rem;border-bottom:1px solid var(--border-color);">Product Name</th>
                        <th style="padding:0.4rem 0.8rem;font-size:0.78rem;border-bottom:1px solid var(--border-color);">Error Message</th>
                    </tr>
                </thead>
                <tbody>${failRows}</tbody>
            </table>
        </div></td>`;
        tbody.appendChild(ntr);
    });
}

function toggleRow(i) {
    const row  = document.getElementById(`nested-${i}`);
    const icon = document.getElementById(`icon-${i}`);
    if (!row) return;
    const visible = row.style.display !== 'none';
    row.style.display  = visible ? 'none' : 'table-row';
    if (icon) icon.style.transform = visible ? 'rotate(0deg)' : 'rotate(90deg)';
}

function renderLogPagination() {
    document.getElementById('logPrevBtn').disabled = logCurrentPage === 0;
    document.getElementById('logNextBtn').disabled = logCurrentPage >= logTotalPages - 1;
    const nav = document.getElementById('logPageNav');
    nav.innerHTML = '';
    for (let i = 0; i < Math.max(1, logTotalPages); i++) {
        if (i === 0 || i === logTotalPages - 1 || Math.abs(i - logCurrentPage) <= 1) {
            const btn = document.createElement('button');
            btn.className  = 'btn-icon';
            btn.textContent = i + 1;
            btn.style.cssText = 'width:28px;height:28px;border-radius:50%;';
            if (i === logCurrentPage) { btn.style.background = 'var(--primary-color)'; btn.style.color = '#fff'; }
            else { btn.style.color = 'var(--text-secondary)'; btn.onclick = () => { logCurrentPage = i; fetchFileLogs(); }; }
            nav.appendChild(btn);
        } else if ((i === 1 && logCurrentPage > 2) || (i === logTotalPages - 2 && logCurrentPage < logTotalPages - 3)) {
            const el = document.createElement('span');
            el.textContent = '…';
            el.style.color  = 'var(--text-secondary)';
            nav.appendChild(el);
        }
    }
}

function changeLogPage(delta) {
    const next = logCurrentPage + delta;
    if (next >= 0 && next < logTotalPages) { logCurrentPage = next; fetchFileLogs(); }
}
