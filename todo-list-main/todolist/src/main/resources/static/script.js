let currentUserId = localStorage.getItem('userId') || null;

// window.onload = function () {
//     const savedName = localStorage.getItem('userName');
//     if (currentUserId && savedName) showDashboard(savedName);
// };

window.onload = function () {

    // 🔥 GOOGLE LOGIN CASE
    const params = new URLSearchParams(window.location.search);
    const googleUserId = params.get("userId");
    const googleName = params.get("name");

    if (googleUserId && googleName) {
        localStorage.setItem("userId", googleUserId);
        localStorage.setItem("userName", googleName);
        showDashboard(googleName);
        return;
    }

    // 🔹 NORMAL LOGIN CASE
    const savedId = localStorage.getItem('userId');
    const savedName = localStorage.getItem('userName');

    if (savedId && savedName) {
        showDashboard(savedName);
    }
};


async function auth(type) {
    const u = document.getElementById('username').value;
    const p = document.getElementById('password').value;

    if (!u || !p) {
        alert("Fill credentials");
        return;
    }

    const res = await fetch(`http://localhost:9090/api/auth/${type}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            email: u,        // 🔥 FIXED
            password: p
        })
    });

    const data = await res.text();

    if (!isNaN(data)) {
        currentUserId = data;
        localStorage.setItem('userId', data);
        localStorage.setItem('userName', u);
        showDashboard(u);
    } else {
        alert(data);
    }
}

function showDashboard(name) {
    document.getElementById('auth-container').style.display = 'none';
    document.getElementById('todo-container').style.display = 'block';
    document.getElementById('user-display').innerText = name;
    loadTasks();
}

async function addTask() {
    const task = {
        description: document.getElementById('taskDesc').value,
        priority: document.getElementById('taskPriority').value,
        status: document.getElementById('taskStatus').value,
        deadline: document.getElementById('taskDeadline').value,
        userId: currentUserId
    };

    await fetch('http://localhost:9090/api/tasks/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task)
    });

    document.getElementById('taskDesc').value = '';
    loadTasks();
}

async function loadTasks() {
    const res = await fetch(`http://localhost:9090/api/tasks/user/${currentUserId}`);
    const tasks = await res.json();
    const list = document.getElementById('taskList');
    list.innerHTML = '';

    tasks.forEach(t => {
        list.innerHTML += `
        <div class="task-card ${t.priority}">
            <h3>${t.description}</h3>
            <small>Deadline: ${t.deadline}</small><br>
            <strong>${t.priority} | ${t.status}</strong>
            <button onclick="deleteTask(${t.id})" style="float:right;color:red">🗑</button>
        </div>`;
    });
}

function logout() {
    localStorage.clear();
    location.reload();
}

async function deleteTask(id) {
    await fetch(`http://localhost:9090/api/tasks/delete/${id}`, { method: 'DELETE' });
    loadTasks();
}
