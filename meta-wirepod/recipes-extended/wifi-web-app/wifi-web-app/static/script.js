document.getElementById('scanBtn').addEventListener('click', async () => {
    document.getElementById('step1').style.display = 'none';
    document.getElementById('step2').style.display = 'block';
    
    const response = await fetch('/scan');
    const networks = await response.json();
    
    const networkSelect = document.getElementById('networkSelect');
    networks.forEach(network => {
        const option = document.createElement('option');
        option.value = network.ssid;
        option.textContent = network.ssid;
        networkSelect.appendChild(option);
    });
    
    document.getElementById('step2').style.display = 'none';
    document.getElementById('step3').style.display = 'block';
    
    networkSelect.addEventListener('change', () => {
        const selectedNetwork = networkSelect.value;
        if (selectedNetwork) {
            document.getElementById('selectedNetwork').textContent = selectedNetwork;
            document.getElementById('step3').style.display = 'none';
            document.getElementById('step4').style.display = 'block';
        }
    });
});

document.getElementById('connectBtn').addEventListener('click', async () => {
    const ssid = document.getElementById('selectedNetwork').textContent;
    const password = document.getElementById('password').value;
    
    document.getElementById('step4').style.display = 'none';
    document.getElementById('step5').style.display = 'block';
    
    const response = await fetch('/connect', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ ssid, password })
    });
    
    if (response.ok) {
        document.getElementById('step5').style.display = 'none';
        document.getElementById('step6').style.display = 'block';
    } else {
        alert('Failed to connect.');
        document.getElementById('step5').style.display = 'none';
        document.getElementById('step4').style.display = 'block';
    }
});
