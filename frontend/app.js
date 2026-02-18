// API Base URL - your Spring Boot service
const API_BASE_URL = CONFIG.API_BASE_URL;

// Global variables
let map;
let markers = [];
let allShops = [];

// Initialize map
function initMap() {
    // Center on Bay Area
    const bayArea = { lat: 37.7749, lng: -122.4194 };
    
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 10,
        center: bayArea,
        styles: [
            {
                featureType: 'poi',
                elementType: 'labels',
                stylers: [{ visibility: 'off' }]
            }
        ]
    });
    
    // Load shops
    loadShops();
}

// Load shops from API
async function loadShops() {
    try {
        const response = await fetch(API_BASE_URL);
        
        if (!response.ok) {
            throw new Error('Failed to fetch shops');
        }
        
        allShops = await response.json();
        
        displayShops(allShops);
        loadCityFilter();
        addMarkersToMap(allShops);
        
    } catch (error) {
        console.error('Error loading shops:', error);
        document.getElementById('shopList').innerHTML = 
            '<p class="loading" style="color: red;">Error loading shops. Make sure Spring Boot is running on port 8081.</p>';
    }
}

// Display shops in sidebar
function displayShops(shops) {
    const shopList = document.getElementById('shopList');
    
    if (shops.length === 0) {
        shopList.innerHTML = '<p class="loading">No shops found.</p>';
        return;
    }
    
    shopList.innerHTML = shops.map(shop => `
        <div class="shop-card" onclick="showShopDetails(${shop.id})">
            <h3>${shop.name}</h3>
            <p class="city">📍 ${shop.city}</p>
            <p class="address">${shop.address}</p>
        </div>
    `).join('');
}

// Load city filter options
async function loadCityFilter() {
    try {
        const response = await fetch(`${API_BASE_URL}/cities`);
        const cities = await response.json();
        
        const cityFilter = document.getElementById('cityFilter');
        
        // Add city options
        cities.forEach(city => {
            const label = document.createElement('label');
            label.innerHTML = `
                <input type="radio" name="city" value="${city}">
                ${city}
            `;
            cityFilter.appendChild(label);
        });
        
        // Add event listeners
        document.querySelectorAll('input[name="city"]').forEach(radio => {
            radio.addEventListener('change', filterByCity);
        });
        
    } catch (error) {
        console.error('Error loading cities:', error);
    }
}

// Filter shops by city
function filterByCity(event) {
    const selectedCity = event.target.value;
    
    if (selectedCity === '') {
        // Show all shops
        displayShops(allShops);
        addMarkersToMap(allShops);
    } else {
        // Filter shops
        const filteredShops = allShops.filter(shop => shop.city === selectedCity);
        displayShops(filteredShops);
        addMarkersToMap(filteredShops);
        
        // Center map on first shop in city
        if (filteredShops.length > 0) {
            map.setCenter({ lat: filteredShops[0].latitude, lng: filteredShops[0].longitude });
            map.setZoom(12);
        }
    }
}

// Add markers to map
function addMarkersToMap(shops) {
    // Clear existing markers
    markers.forEach(marker => marker.setMap(null));
    markers = [];
    
    // Add new markers
    shops.forEach(shop => {
        const marker = new google.maps.Marker({
            position: { lat: shop.latitude, lng: shop.longitude },
            map: map,
            title: shop.name,
            animation: google.maps.Animation.DROP
        });
        
        // Click marker to show details
        marker.addListener('click', () => {
            showShopDetails(shop.id);
        });
        
        markers.push(marker);
    });
    
    // Fit map to show all markers
    if (markers.length > 0) {
        const bounds = new google.maps.LatLngBounds();
        markers.forEach(marker => bounds.extend(marker.getPosition()));
        map.fitBounds(bounds);
    }
}

// Show shop details in modal
function showShopDetails(shopId) {
    const shop = allShops.find(s => s.id === shopId);
    
    if (!shop) return;
    
    const modal = document.getElementById('shopModal');
    const shopDetails = document.getElementById('shopDetails');
    
    shopDetails.innerHTML = `
        <h2>${shop.name}</h2>
        <p><strong>📍 Location:</strong> ${shop.city}</p>
        <p><strong>🏠 Address:</strong> ${shop.address}</p>
        ${shop.phoneNumber ? `<p><strong>📞 Phone:</strong> ${shop.phoneNumber}</p>` : ''}
        ${shop.website ? `<p><strong>🌐 Website:</strong> <a href="${shop.website}" target="_blank">${shop.website}</a></p>` : ''}
        ${shop.description ? `<p><strong>📝 Description:</strong> ${shop.description}</p>` : ''}
        <p><strong>🗺️ Coordinates:</strong> ${shop.latitude}, ${shop.longitude}</p>
    `;
    
    modal.style.display = 'block';
    
    // Center map on shop
    map.setCenter({ lat: shop.latitude, lng: shop.longitude });
    map.setZoom(15);
}

// Close modal
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('shopModal');
    const closeBtn = document.querySelector('.close');
    
    closeBtn.onclick = () => {
        modal.style.display = 'none';
    };
    
    window.onclick = (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    };
});