// ==========================================================
// LAB 03 - PHẦN B: Script Quản Lý Sinh Viên Gọi REST API
// ==========================================================

const API_BASE = window.location.origin.startsWith("http")
    ? "/api/students"
    : "http://localhost:8080/api/students";

document.addEventListener("DOMContentLoaded", () => {
    loadStudents();
    setupEventListeners();
});

// 1. Tải danh sách sinh viên từ API (Hỗ trợ tìm kiếm theo keyword)
async function loadStudents(keyword = "") {
    const tbody = document.getElementById("studentTableBody");
    if (tbody) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center py-4 text-muted">
                    <div class="spinner-border spinner-border-sm text-primary me-2" role="status"></div>
                    Đang tải dữ liệu từ API...
                </td>
            </tr>
        `;
    }

    try {
        let url = API_BASE;
        if (keyword && keyword.trim() !== "") {
            url += `?keyword=${encodeURIComponent(keyword.trim())}`;
        }

        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const students = await response.json();
        renderStudents(students);
    } catch (error) {
        console.error("Lỗi khi tải danh sách sinh viên:", error);
        if (tbody) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-danger py-4">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        Không thể tải dữ liệu từ REST API (${error.message}). Vui lòng kiểm tra server Spring Boot!
                    </td>
                </tr>
            `;
        }
    }
}

// 2. Render danh sách sinh viên ra bảng HTML (Theo chuẩn Bước 2 Phần B)
function renderStudents(students) {
    const tbody = document.getElementById("studentTableBody");
    if (!tbody) return;

    tbody.innerHTML = "";

    if (!students || students.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center text-muted py-4">
                    <i class="bi bi-inbox fs-4 d-block mb-1"></i>
                    Không tìm thấy sinh viên nào phù hợp.
                </td>
            </tr>
        `;
        return;
    }

    students.forEach(student => {
        const studentId = student.id || "";
        const studentCode = escapeHtml(student.studentCode || "");
        const fullName = escapeHtml(student.fullName || "");
        const email = escapeHtml(student.email || "");
        const phone = escapeHtml(student.phone || "");
        const className = escapeHtml(student.className || "");

        const row = `
            <tr>
                <td><span class="badge bg-primary-subtle text-primary font-monospace fw-bold">${studentCode}</span></td>
                <td class="fw-semibold text-dark">${fullName}</td>
                <td><a href="mailto:${email}" class="text-decoration-none text-secondary">${email}</a></td>
                <td>${phone ? `<a href="tel:${phone}" class="text-decoration-none text-secondary">${phone}</a>` : '<span class="text-muted fst-italic">Chưa có</span>'}</td>
                <td><span class="badge bg-warning-subtle text-warning-emphasis">${className || 'N/A'}</span></td>
                <td class="text-end">
                    <div class="table-actions d-flex justify-content-end gap-1">
                        <!-- Xem -->
                        <a
                            class="btn btn-info btn-sm text-white"
                            href="index.html?page=form&id=${encodeURIComponent(studentId)}&mode=view"
                            onclick="handleViewStudent(event, '${studentId}')"
                            title="Xem chi tiết"
                        >
                            <i class="bi bi-eye"></i>
                        </a>
                        <!-- Sửa -->
                        <a
                            class="btn btn-warning btn-sm text-dark"
                            href="index.html?page=form&id=${encodeURIComponent(studentId)}&mode=edit"
                            onclick="handleEditStudent(event, '${studentId}')"
                            title="Chỉnh sửa"
                        >
                            <i class="bi bi-pencil-square"></i>
                        </a>
                        <!-- Xóa -->
                        <button
                            class="btn btn-danger btn-sm"
                            onclick="deleteStudent('${studentId}', '${fullName}')"
                            title="Xóa sinh viên"
                        >
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    });

    const countElem = document.getElementById("studentCountBadge");
    if (countElem) {
        countElem.textContent = `${students.length} sinh viên`;
    }
}

// 3. Xóa sinh viên qua API DELETE /api/students/{id}
async function deleteStudent(id, name) {
    if (!id) return;
    const confirmMessage = name
        ? `Bạn có chắc chắn muốn xóa sinh viên "${name}" (ID: ${id}) không?`
        : `Bạn có chắc chắn muốn xóa sinh viên này không?`;

    if (!confirm(confirmMessage)) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            showAlert("success", `Đã xóa thành công sinh viên!`);
            loadStudents(getCurrentSearchKeyword());
        } else {
            const errText = await response.text();
            showAlert("danger", `Lỗi khi xóa sinh viên: ${errText || response.statusText}`);
        }
    } catch (error) {
        console.error("Lỗi xóa sinh viên:", error);
        showAlert("danger", `Lỗi kết nối khi xóa: ${error.message}`);
    }
}

// 4. Lưu sinh viên (Thêm mới POST hoặc Cập nhật PUT)
async function saveStudent(studentData) {
    const isEdit = Boolean(studentData.id);
    const url = isEdit ? `${API_BASE}/${studentData.id}` : API_BASE;
    const method = isEdit ? "PUT" : "POST";

    const response = await fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(studentData)
    });

    if (!response.ok) {
        const errText = await response.text();
        throw new Error(errText || `Lỗi máy chủ (${response.status})`);
    }

    return await response.json();
}

// 5. Lấy thông tin sinh viên theo ID (GET /api/students/{id})
async function getStudentById(id) {
    const response = await fetch(`${API_BASE}/${id}`);
    if (!response.ok) {
        throw new Error(`Không tìm thấy sinh viên ID ${id}`);
    }
    return await response.json();
}

// 6. Xử lý mở Modal Xem / Sửa ngay trên trang nếu có Modal
function handleViewStudent(event, id) {
    const modalElem = document.getElementById("studentModal");
    if (modalElem && window.bootstrap) {
        event.preventDefault();
        openStudentModal("view", id);
    }
}

function handleEditStudent(event, id) {
    const modalElem = document.getElementById("studentModal");
    if (modalElem && window.bootstrap) {
        event.preventDefault();
        openStudentModal("edit", id);
    }
}

// Mở modal Thêm/Sửa/Xem
async function openStudentModal(mode = "add", id = null) {
    const modalElem = document.getElementById("studentModal");
    if (!modalElem || !window.bootstrap) return;

    const modalTitle = document.getElementById("studentModalTitle");
    const form = document.getElementById("studentForm");
    const saveBtn = document.getElementById("btnSaveStudent");
    const idInput = document.getElementById("modalStudentId");
    const codeInput = document.getElementById("modalStudentCode");
    const nameInput = document.getElementById("modalFullName");
    const emailInput = document.getElementById("modalEmail");
    const phoneInput = document.getElementById("modalPhone");
    const classInput = document.getElementById("modalClassName");

    if (form) form.reset();
    idInput.value = "";

    const isView = (mode === "view");
    const isEdit = (mode === "edit");

    if (modalTitle) {
        if (isView) modalTitle.innerHTML = '<i class="bi bi-eye me-2"></i>Chi Tiết Sinh Viên';
        else if (isEdit) modalTitle.innerHTML = '<i class="bi bi-pencil-square me-2"></i>Chỉnh Sửa Sinh Viên';
        else modalTitle.innerHTML = '<i class="bi bi-person-plus me-2"></i>Thêm Sinh Viên Mới';
    }

    [codeInput, nameInput, emailInput, phoneInput, classInput].forEach(input => {
        if (input) input.disabled = isView;
    });

    if (saveBtn) {
        saveBtn.style.display = isView ? "none" : "inline-block";
        saveBtn.textContent = isEdit ? "Lưu thay đổi" : "Thêm mới";
    }

    if ((isView || isEdit) && id) {
        try {
            const student = await getStudentById(id);
            idInput.value = student.id || "";
            codeInput.value = student.studentCode || "";
            nameInput.value = student.fullName || "";
            emailInput.value = student.email || "";
            phoneInput.value = student.phone || "";
            classInput.value = student.className || "";
        } catch (err) {
            alert("Lỗi khi tải thông tin sinh viên: " + err.message);
            return;
        }
    }

    const modal = bootstrap.Modal.getOrCreateInstance(modalElem);
    modal.show();
}

// Thiết lập các sự kiện trên trang (Tìm kiếm, Submit form)
function setupEventListeners() {
    const searchInput = document.getElementById("searchInput");
    if (searchInput) {
        let debounceTimer;
        searchInput.addEventListener("input", (e) => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                loadStudents(e.target.value);
            }, 300);
        });
    }

    const searchForm = document.getElementById("searchForm");
    if (searchForm) {
        searchForm.addEventListener("submit", (e) => {
            e.preventDefault();
            const keyword = getCurrentSearchKeyword();
            loadStudents(keyword);
        });
    }

    const btnResetSearch = document.getElementById("btnResetSearch");
    if (btnResetSearch) {
        btnResetSearch.addEventListener("click", () => {
            if (searchInput) searchInput.value = "";
            loadStudents("");
        });
    }

    const studentForm = document.getElementById("studentForm");
    if (studentForm) {
        studentForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const idVal = document.getElementById("modalStudentId").value.trim();
            const payload = {
                studentCode: document.getElementById("modalStudentCode").value.trim(),
                fullName: document.getElementById("modalFullName").value.trim(),
                email: document.getElementById("modalEmail").value.trim(),
                phone: document.getElementById("modalPhone").value.trim(),
                className: document.getElementById("modalClassName").value.trim()
            };

            if (idVal) {
                payload.id = idVal;
            }

            try {
                await saveStudent(payload);
                showAlert("success", idVal ? "Đã cập nhật sinh viên thành công!" : "Đã thêm mới sinh viên thành công!");
                const modalElem = document.getElementById("studentModal");
                if (modalElem && window.bootstrap) {
                    const modal = bootstrap.Modal.getInstance(modalElem);
                    if (modal) modal.hide();
                }
                loadStudents(getCurrentSearchKeyword());
            } catch (err) {
                alert("Lỗi lưu sinh viên: " + err.message);
            }
        });
    }
}

function getCurrentSearchKeyword() {
    const input = document.getElementById("searchInput");
    return input ? input.value : "";
}

function showAlert(type, message) {
    const container = document.getElementById("alertContainer");
    if (!container) return;

    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show d-flex align-items-center" role="alert">
            <i class="bi ${type === 'success' ? 'bi-check-circle-fill' : 'bi-exclamation-triangle-fill'} me-2"></i>
            <div>${escapeHtml(message)}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
    container.innerHTML = alertHtml;
    setTimeout(() => {
        const alertElem = container.querySelector(".alert");
        if (alertElem && window.bootstrap) {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alertElem);
            if (bsAlert) bsAlert.close();
        }
    }, 4000);
}

function escapeHtml(text) {
    if (!text) return "";
    return String(text)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
