document.addEventListener('DOMContentLoaded', () => {
    // State management
    let currentPage = 0;
    const pageSize = 5;
    let currentSearch = '';
    let currentCarrierId = null;
    let currentOrgId = null;
    let currentOrgType = null;

    // Elements - List View
    const organizationsListView = document.getElementById('organizationsListView');
    const organizationsTableBody = document.getElementById('organizationsTableBody');
    const orgSearchInput = document.getElementById('orgSearchInput');
    const prevPageBtn = document.getElementById('prevPage');
    const nextPageBtn = document.getElementById('nextPage');
    const orgPagingInfo = document.getElementById('orgPagingInfo');
    const btnAddOrganization = document.getElementById('btnAddOrganization');

    // Elements - Association View
    const organizationAssociationView = document.getElementById('organizationAssociationView');
    const associationOrgName = document.getElementById('associationOrgName');
    const btnBackToOrgs = document.getElementById('btnBackToOrgs');
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    const linkedShippersTableBody = document.getElementById('linkedShippersTableBody');
    const linkedWarehousesTableBody = document.getElementById('linkedWarehousesTableBody');
    const linkedCarriersTableBody = document.getElementById('linkedCarriersTableBody');
    const btnOpenLinkShipperModal = document.getElementById('btnOpenLinkShipperModal');
    const btnAddWarehouseMain = document.getElementById('btnAddWarehouseMain');
    const btnOpenLinkWarehouseModalMain = document.getElementById('btnOpenLinkWarehouseModalMain'); // may be null
    const tabShipper = document.getElementById('tabShipper');
    const tabCarrier = document.getElementById('tabCarrier');

    // Elements - Org Modal
    const orgModalOverlay = document.getElementById('orgModalOverlay');
    const orgForm = document.getElementById('orgForm');
    const orgModalTitle = document.getElementById('orgModalTitle');
    const btnCancelOrg = document.getElementById('btnCancelOrg');

    // Elements - Link Shipper Modal
    const linkShipperModalOverlay = document.getElementById('linkShipperModalOverlay');
    const allShippersTableBody = document.getElementById('allShippersTableBody');
    const shipperSearchInput = document.getElementById('shipperSearchInput');
    const btnSaveShipperLinks = document.getElementById('btnSaveShipperLinks');
    const btnCancelLinkShipper = document.getElementById('btnCancelLinkShipper');

    // Elements - Link Carrier Modal
    const linkCarrierModalOverlay = document.getElementById('linkCarrierModalOverlay');
    const allCarriersTableBody = document.getElementById('allCarriersTableBody');
    const carrierSearchInput = document.getElementById('carrierSearchInput');
    const btnSaveCarrierLinks = document.getElementById('btnSaveCarrierLinks');
    const btnCancelLinkCarrier = document.getElementById('btnCancelLinkCarrier');

    // Elements - Link Warehouse Modal
    const linkWarehouseModalOverlay = document.getElementById('linkWarehouseModalOverlay');
    const allWarehousesTableBody = document.getElementById('allWarehousesTableBody');
    const btnSaveWarehouseLink = document.getElementById('btnSaveWarehouseLink');
    const btnCancelLinkWarehouse = document.getElementById('btnCancelLinkWarehouse');
    const targetShipperNameSpan = document.getElementById('targetShipperName');
    let currentLinkShipperId = null;

    // --- Core Functions ---

    async function fetchOrganizations(page = 0, search = '') {
        try {
            const response = await fetch(`/api/organizations?pageNumber=${page}&size=${pageSize}&search=${search}`);
            const result = await response.json();
            if (result.success) {
                renderOrganizations(result.data.organizations);
                updatePagination(result.data.metaData);
            } else {
                showToast(result.message, 'error');
            }
        } catch (error) {
            showToast('Failed to fetch organizations', 'error');
        }
    }

    function renderOrganizations(organizations) {
        organizationsTableBody.innerHTML = '';
        organizations.forEach((org, index) => {
            const tr = document.createElement('tr');
            const orgId = org.organizationId || org.id;
            tr.innerHTML = `
                <td>${currentPage * pageSize + index + 1}</td>
                <td>${org.organizationName}</td>
                <td>${org.organizationCode}</td>
                <td><span class="badge ${org.organizationType === 'CARRIER' ? 'badge-success' : (org.organizationType === 'SHIPPER' ? 'badge-warning' : 'badge-info')}">${ org.organizationType}</span></td>
                <td>${org.contactEmail || '-'}</td>
                <td style="text-align: right;">
                    <button class="btn-icon btn-edit" data-id="${orgId}" title="Edit">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                    </button>
                    <button class="btn-icon btn-delete" data-id="${orgId}" title="Delete">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
                    </button>
                    ${(org.organizationType === 'CARRIER' || org.organizationType === 'SHIPPER') ? `
                    <button class="btn-icon btn-associate" data-id="${orgId}" data-name="${org.organizationName}" data-type="${org.organizationType}" title="Association">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"></circle><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path></svg>
                    </button>` : ''}
                </td>
            `;
            organizationsTableBody.appendChild(tr);
        });

        // Event listeners for actions
        document.querySelectorAll('.btn-edit').forEach(btn => btn.onclick = () => editOrganization(btn.dataset.id));
        document.querySelectorAll('.btn-delete').forEach(btn => btn.onclick = () => deleteOrganization(btn.dataset.id));
        document.querySelectorAll('.btn-associate').forEach(btn => btn.onclick = (e) => {
            const target = e.currentTarget;
            openAssociation(target.dataset.id, target.dataset.name, target.dataset.type);
        });
    }

    function updatePagination(metaData) {
        const start = metaData.pageNumber * metaData.pageSize + 1;
        const end = Math.min(start + metaData.pageSize - 1, metaData.recordCount);
        orgPagingInfo.innerText = `Showing ${start} to ${end} of ${metaData.recordCount} records`;
        prevPageBtn.disabled = metaData.pageNumber === 0;
        nextPageBtn.disabled = metaData.pageNumber >= metaData.pageCount - 1;
    }

    // --- Organization Modal ---

    btnAddOrganization.onclick = () => {
        orgForm.reset();
        document.getElementById('orgId').value = '';
        orgModalTitle.innerText = 'Add Organization';
        orgModalOverlay.classList.add('active');
    };

    btnCancelOrg.onclick = () => orgModalOverlay.classList.remove('active');
    document.querySelector('#orgModalOverlay .btn-close').onclick = () => orgModalOverlay.classList.remove('active');

    orgForm.onsubmit = async (e) => {
        e.preventDefault();
        const orgId = document.getElementById('orgId').value;
        const data = {
            organizationName: document.getElementById('orgName').value,
            organizationCode: document.getElementById('orgCode').value,
            organizationType: document.getElementById('orgType').value,
            contactEmail: document.getElementById('orgEmail').value,
            website: document.getElementById('orgWebsite').value,
            address: document.getElementById('orgAddr1').value,
            city: document.getElementById('orgCity').value,
            state: document.getElementById('orgState').value,
            country: document.getElementById('orgCountry').value,
            zipCode: document.getElementById('orgZip').value,
            inboundEmails: document.getElementById('inboundEmails').value,
            outboundEmails: document.getElementById('outboundEmails').value
        };

        const url = orgId ? `/api/update-organization/${orgId}` : '/api/create-organization';
        const method = orgId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            const result = await response.json();
            if (result.success) {
                showToast(result.message, 'success');
                orgModalOverlay.classList.remove('active');
                fetchOrganizations(currentPage, currentSearch);
            } else {
                showToast(result.message, 'error');
            }
        } catch (error) {
            showToast('Failed to save organization', 'error');
        }
    };

    async function editOrganization(id) {
        try {
            const response = await fetch(`/api/organizations?search=&size=1000`);
            const result = await response.json();
            if (result.success) {
                const org = result.data.organizations.find(o => (o.organizationId || o.id) == id);
                if (org) {
                    document.getElementById('orgId').value = org.organizationId || org.id;
                    document.getElementById('orgName').value = org.organizationName;
                    document.getElementById('orgCode').value = org.organizationCode;
                    document.getElementById('orgType').value = org.organizationType;
                    document.getElementById('orgEmail').value = org.contactEmail || '';
                    document.getElementById('orgWebsite').value = org.website || '';
                    document.getElementById('orgAddr1').value = org.address || '';
                    document.getElementById('orgCity').value = org.city || '';
                    document.getElementById('orgState').value = org.state || '';
                    document.getElementById('orgCountry').value = org.country || '';
                    document.getElementById('orgZip').value = org.zipCode || '';
                    
                    orgModalTitle.innerText = 'Update Organization';
                    orgModalOverlay.classList.add('active');
                }
            }
        } catch (error) {
            showToast('Failed to load organization details', 'error');
        }
    }

    async function deleteOrganization(id) {
        if (!confirm('Are you sure you want to delete this organization?')) return;
        try {
            const response = await fetch(`/api/delete-organization/${id}`, { method: 'DELETE' });
            const result = await response.json();
            if (result.success) {
                showToast(result.message, 'success');
                fetchOrganizations(currentPage, currentSearch);
            } else {
                showToast(result.message, 'error');
            }
        } catch (error) {
            showToast('Failed to delete organization', 'error');
        }
    }

    // --- Association Management ---

    function openAssociation(id, name, type) {
        currentOrgId = id;
        currentOrgType = type;
        associationOrgName.innerText = name;
        organizationsListView.style.display = 'none';
        organizationAssociationView.style.display = 'block';

        // Reset tabs and visibility
        tabButtons.forEach(b => {
            b.classList.remove('active');
            if (b.dataset.tab === 'shipperTab') b.style.display = (type === 'CARRIER' ? 'block' : 'none');
            if (b.dataset.tab === 'carrierTab') b.style.display = (type === 'SHIPPER' ? 'block' : 'none');
        });
        tabContents.forEach(c => c.classList.remove('active'));
        
        // Activate first tab (Users)
        tabButtons[0].classList.add('active');
        tabContents[0].classList.add('active');
        
        loadAssociations();
    }

    btnBackToOrgs.onclick = () => {
        organizationAssociationView.style.display = 'none';
        organizationsListView.style.display = 'block';
        fetchOrganizations(currentPage, currentSearch);
    };

    tabButtons.forEach(btn => {
        btn.onclick = () => {
            tabButtons.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            btn.classList.add('active');
            document.getElementById(btn.dataset.tab).classList.add('active');
            loadAssociations();
        };
    });

    async function loadAssociations() {
        const activeTab = document.querySelector('.tab-btn.active').dataset.tab;
        if (activeTab === 'shipperTab') {
            fetchLinkedShippers();
        } else if (activeTab === 'carrierTab') {
            fetchLinkedCarriers();
        } else if (activeTab === 'warehouseTab') {
            fetchLinkedWarehouses();
        }
    }

    async function fetchLinkedShippers() {
        try {
            const response = await fetch(`/api/organizations/shippers?carrierId=${currentOrgId}`);
            const result = await response.json();
            if (result.success) {
                renderLinkedShippers(result.data.filter(s => s.association === 'ACTIVE'));
            }
        } catch (error) {
            showToast('Failed to fetch linked shippers', 'error');
        }
    }

    async function fetchLinkedCarriers() {
        try {
            const response = await fetch(`/api/organizations/carriers?shipperId=${currentOrgId}`);
            const result = await response.json();
            if (result.success) {
                renderLinkedCarriers(result.data.filter(c => c.association === 'ACTIVE'));
            }
        } catch (error) {
            showToast('Failed to fetch linked carriers', 'error');
        }
    }

    function renderLinkedCarriers(carriers) {
        linkedCarriersTableBody.innerHTML = '';
        carriers.forEach((carrier, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${carrier.organizationName}</td>
                <td>${carrier.organizationCode}</td>
                <td>${carrier.organizationContact || '-'}</td>
                <td>${carrier.contactEmail || '-'}</td>
                <td style="text-align: right;">
                    <button class="btn-icon" title="Delete Association"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg></button>
                </td>
            `;
            linkedCarriersTableBody.appendChild(tr);
        });
    }

    function renderLinkedShippers(shippers) {
        linkedShippersTableBody.innerHTML = '';
        shippers.forEach((shipper, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${shipper.organizationName}</td>
                <td>${shipper.organizationCode}</td>
                <td>${shipper.organizationContact || '-'}</td>
                <td>${shipper.contactEmail || '-'}</td>
                <td>${shipper.city || '-'}</td>
                <td>${shipper.state || '-'}</td>
                <td style="text-align: right;">
                    <button class="btn-icon btn-link-warehouse" data-id="${shipper.organizationId || shipper.id}" data-name="${shipper.organizationName}" title="Link Warehouse">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"></path><path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"></path></svg>
                    </button>
                    <button class="btn-icon" title="Delete Association"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg></button>
                </td>
            `;
            linkedShippersTableBody.appendChild(tr);
        });

        document.querySelectorAll('.btn-link-warehouse').forEach(btn => btn.onclick = () => openLinkWarehouseModal(btn.dataset.id, btn.dataset.name));
        document.querySelectorAll('.btn-associate-settings').forEach(btn => btn.onclick = () => openLinkShipperModal());
    }

    async function fetchLinkedWarehouses() {
        try {
            const url = currentOrgType === 'CARRIER' 
                ? `/api/organizations/warehouses?carrierId=${currentOrgId}`
                : `/api/organizations/warehouses`;
            const response = await fetch(url);
            const result = await response.json();
            if (result.success) {
                renderLinkedWarehouses(result.data.filter(w => w.association === 'ACTIVE' || currentOrgType === 'SHIPPER'));
            }
        } catch (error) {
            showToast('Failed to fetch linked warehouses', 'error');
        }
    }

    function renderLinkedWarehouses(warehouses) {
        linkedWarehousesTableBody.innerHTML = '';
        if (warehouses.length === 0) {
            linkedWarehousesTableBody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:var(--text-secondary);">No warehouses linked yet</td></tr>';
            return;
        }
        warehouses.forEach((w, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${w.organizationName}</td>
                <td>${w.organizationCode}</td>
                <td>-</td>
                <td><span class="badge badge-success">ACTIVE</span></td>
            `;
            linkedWarehousesTableBody.appendChild(tr);
        });
    }

    // --- Link Shipper Modal Logic ---

    function renderAllShippersForLinking(shippers) {
        allShippersTableBody.innerHTML = '';
        shippers.forEach((shipper, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${shipper.organizationName}</td>
                <td>${shipper.address || '-'}</td>
                <td>${shipper.city || '-'}</td>
                <td>${shipper.state || '-'}</td>
                <td style="text-align: center;">
                    <label class="toggle-switch">
                        <input type="checkbox" class="shipper-link-checkbox" data-id="${shipper.organizationId || shipper.id}" ${shipper.association === 'ACTIVE' || shipper.association === '1' ? 'checked' : ''}>
                        <span class="slider"></span>
                    </label>
                </td>
            `;
            allShippersTableBody.appendChild(tr);
        });
    }

    btnCancelLinkShipper.onclick = () => linkShipperModalOverlay.classList.remove('active');
    document.querySelector('#linkShipperModalOverlay .btn-close').onclick = () => linkShipperModalOverlay.classList.remove('active');

    btnSaveShipperLinks.onclick = async () => {
        const selectedShippers = Array.from(document.querySelectorAll('.shipper-link-checkbox'))
            .map(cb => ({
                shipperId: parseInt(cb.dataset.id),
                association: cb.checked ? 'ACTIVE' : 'INACTIVE'
            }));

        const data = {
            organizations: selectedShippers
        };

        try {
            const response = await fetch(`/api/link-shipper?carrierId=${currentOrgId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            const result = await response.json();
            if (result.success) {
                showToast('Shippers linked successfully', 'success');
                linkShipperModalOverlay.classList.remove('active');
                fetchLinkedShippers();
            } else {
                showToast(result.message, 'error');
            }
        } catch (error) {
            showToast('Failed to link shippers', 'error');
        }
    };

    // --- Link/Add Organization Modal Logic ---

    if (btnAddWarehouseMain) {
        btnAddWarehouseMain.onclick = () => {
            orgForm.reset();
            document.getElementById('orgId').value = '';
            document.getElementById('orgType').value = 'WAREHOUSE';
            orgModalTitle.innerText = 'Add Warehouse';
            orgModalOverlay.classList.add('active');
        };
    }

    const openLinkWarehouseModal = async (shipperId, shipperName) => {
        currentLinkShipperId = shipperId;
        targetShipperNameSpan.innerText = shipperName;
        try {
            const response = await fetch(`/api/organizations/warehouses?carrierId=${currentOrgId}`);
            const result = await response.json();
            if (result.success) {
                renderAllWarehousesForLinking(result.data);
                linkWarehouseModalOverlay.classList.add('active');
            }
        } catch (error) {
            showToast('Failed to load warehouses', 'error');
        }
    };

    if (btnOpenLinkWarehouseModalMain) {
        btnOpenLinkWarehouseModalMain.onclick = () => {
            if (currentOrgType === 'SHIPPER') {
                openLinkWarehouseModal(currentOrgId, associationOrgName.innerText);
            } else {
                showToast('Please link a warehouse via a specific Shipper from the Shipper tab', 'info');
                const shipperTabBtn = document.querySelector('[data-tab="shipperTab"]');
                if (shipperTabBtn) shipperTabBtn.click();
            }
        };
    }

    const openLinkShipperModal = async () => {
        try {
            const response = await fetch(`/api/organizations/shippers?carrierId=${currentOrgId}`);
            const result = await response.json();
            if (result.success) {
                renderAllShippersForLinking(result.data);
                linkShipperModalOverlay.classList.add('active');
            }
        } catch (error) {
            showToast('Failed to load shippers', 'error');
        }
    };

    btnOpenLinkShipperModal.onclick = openLinkShipperModal;

    const openLinkCarrierModal = async () => {
        try {
            const response = await fetch(`/api/organizations/carriers?shipperId=${currentOrgId}`);
            const result = await response.json();
            if (result.success) {
                renderAllCarriersForLinking(result.data);
                linkCarrierModalOverlay.classList.add('active');
            }
        } catch (error) {
            showToast('Failed to load carriers', 'error');
        }
    };

    const btnOpenLinkCarrierModal = document.getElementById('btnOpenLinkCarrierModal');
    if (btnOpenLinkCarrierModal) btnOpenLinkCarrierModal.onclick = openLinkCarrierModal;

    function renderAllCarriersForLinking(carriers) {
        allCarriersTableBody.innerHTML = '';
        carriers.forEach((carrier, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${carrier.organizationName}</td>
                <td>${carrier.organizationCode}</td>
                <td>${carrier.city || '-'}</td>
                <td>${carrier.state || '-'}</td>
                <td style="text-align: center;">
                    <label class="toggle-switch">
                        <input type="checkbox" class="carrier-link-checkbox" data-id="${carrier.organizationId || carrier.id}" ${carrier.association === 'ACTIVE' ? 'checked' : ''}>
                        <span class="slider"></span>
                    </label>
                </td>
            `;
            allCarriersTableBody.appendChild(tr);
        });
    }

    btnCancelLinkCarrier.onclick = () => linkCarrierModalOverlay.classList.remove('active');
    document.querySelector('#linkCarrierModalOverlay .btn-close').onclick = () => linkCarrierModalOverlay.classList.remove('active');

    btnSaveCarrierLinks.onclick = async () => {
        const selectedCarriers = Array.from(document.querySelectorAll('.carrier-link-checkbox'))
            .filter(cb => cb.checked)
            .map(cb => ({
                carrierId: parseInt(cb.dataset.id),
                shipperId: parseInt(currentOrgId),
                association: 'ACTIVE'
            }));
        
        for (const carrier of selectedCarriers) {
            try {
                await fetch(`/api/link-shipper?carrierId=${carrier.carrierId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        organizations: [{
                            shipperId: carrier.shipperId,
                            association: 'ACTIVE'
                        }]
                    })
                });
            } catch (error) {
                console.error('Failed to link carrier', carrier.carrierId, error);
            }
        }
        
        showToast('Associations updated successfully', 'success');
        linkCarrierModalOverlay.classList.remove('active');
        fetchLinkedCarriers();
    };

    function renderAllWarehousesForLinking(warehouses) {
        allWarehousesTableBody.innerHTML = '';
        warehouses.forEach((w, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${w.organizationName}</td>
                <td>${w.organizationCode}</td>
                <td style="text-align: center;">
                    <label class="toggle-switch">
                        <input type="checkbox" class="warehouse-link-checkbox" data-id="${w.organizationId || w.id}" ${(w.association === 'ACTIVE' || w.association === '1') ? 'checked' : ''}>
                        <span class="slider"></span>
                    </label>
                </td>
            `;
            allWarehousesTableBody.appendChild(tr);
        });
    }

    btnCancelLinkWarehouse.onclick = () => linkWarehouseModalOverlay.classList.remove('active');
    document.querySelector('#linkWarehouseModalOverlay .btn-close').onclick = () => linkWarehouseModalOverlay.classList.remove('active');

    btnSaveWarehouseLink.onclick = async () => {
        const selectedWarehouses = Array.from(document.querySelectorAll('.warehouse-link-checkbox'))
            .map(cb => ({
                shipperId: parseInt(currentLinkShipperId),
                warehouseId: parseInt(cb.dataset.id),
                association: cb.checked ? 'ACTIVE' : 'INACTIVE'
            }));

        const data = { organizations: selectedWarehouses };
        try {
            const response = await fetch(`/api/link-warehouse?carrierId=${currentOrgId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            const result = await response.json();
            if (result.success) {
                showToast('Warehouse linked successfully', 'success');
                linkWarehouseModalOverlay.classList.remove('active');
                if (document.querySelector('.tab-btn.active').dataset.tab === 'warehouseTab') {
                    fetchLinkedWarehouses();
                }
            } else {
                showToast(result.message, 'error');
            }
        } catch (error) {
            showToast('Failed to link warehouse', 'error');
        }
    };

    // --- Helpers ---

    function showToast(message, type = 'success') {
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.innerHTML = `
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                ${type === 'success' ? '<polyline points="20 6 9 17 4 12"></polyline>' : '<circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line>'}
            </svg>
            <span>${message}</span>
        `;
        const container = document.getElementById('toastContainer');
        container.appendChild(toast);
        setTimeout(() => {
            toast.classList.add('fade-out');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    // --- Search & Pagination Listeners ---

    orgSearchInput.oninput = (e) => {
        currentSearch = e.target.value;
        currentPage = 0;
        fetchOrganizations(currentPage, currentSearch);
    };

    prevPageBtn.onclick = () => {
        if (currentPage > 0) {
            currentPage--;
            fetchOrganizations(currentPage, currentSearch);
        }
    };

    nextPageBtn.onclick = () => {
        currentPage++;
        fetchOrganizations(currentPage, currentSearch);
    };

    // Initial load
    fetchOrganizations();
});
