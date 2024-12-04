import { createApp, ref, onMounted } from './vue.esm-browser.js'
const createLicenseModalVisiable = ref(false)

const licenseList = ref([
    { licenseName: 'License 1', description: 'Description 1', expireDate: '2023-05-31' },
    { licenseName: 'License 2', description: 'Description 2', expireDate: '2023-06-15' },
])

fetch('http://127.0.0.1:9001/licenseServer/listLicense')
    .then(response => response.json())
    .then(data => licenseList.value = data)
    .catch(error => console.error('Error fetching data:', error))

const s = {
    setup() {
        return { licenseList, createLicenseModalVisiable }
    }
}

createApp(s).mount('#app')


