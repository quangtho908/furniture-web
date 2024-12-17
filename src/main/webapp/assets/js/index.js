const btn = document.getElementById("menu_btn-responsive");
const dropdown = document.querySelector("#menu_btn-responsive ~ .menu_list")

btn?.addEventListener("click", function() {
    dropdown.classList.toggle("menu_list_block")
    this.classList.toggle("is-open")

    this.innerHTML = btn.getAttribute("class").split(" ")[2] ? '<i class="fa-solid fa-xmark"></i>' : '<i class="fa-solid fa-bars"></i>'
})

const menu = document.querySelector("menu");
let point = 0;
if(menu) {
    window.addEventListener("scroll", function() {
        if((this.scrollY > point)) {
            menu?.classList.add("hidden_menu");
        }else {
            menu?.classList.remove("hidden_menu");
        }
        point = this.scrollY;

        if(point > 0) {
            menu?.classList.add("scroll_menu")
        }else {
            menu?.classList.remove("scroll_menu")
        }
    })
}

const slide = document.querySelector(".head_bottom-slider")
if(slide) {
    const slider = tns({
        container: ".head_bottom-slider",
        items: 1,
        mouseDrag: true,
        controls: false,
        navPosition: "bottom"
    })
}

const gallery = document.querySelector('.product-gallery')

if(gallery) {
    const slider = tns({
        container: ".product-gallery",
        item: 1,
        controls: false,
        mouseDrag: true,
        navContainer: ".gallery-action",
    })
}


const allStarRates = document.querySelectorAll(".star-rate");

allStarRates.forEach((rate, index) => {
    const rate_point = parseFloat(rate.getAttribute("data-rate"));
    let star_fill = Math.floor(rate_point);
    const part_star = rate_point - star_fill;

    let i = 0;
    while( i < star_fill) {
        rate.innerHTML += '<span><i class="fa-solid fa-star"></i></span>'
        i++;
    }

    if(part_star >= 0.5) {
        rate.innerHTML += '<span><i class="fa-solid fa-star-half-stroke"></i></span>'
        star_fill++;
    }

    let j = 0;
    while(j < (5 - star_fill)) {
        rate.innerHTML += '<span class="no-point"><i class="fa-regular fa-star"></i></span>'
        j++
    }
})

const category_slide = document.querySelector(".category-slide")

if(category_slide) {
    tns({
        container: ".category-slide",
        items: 3,
        nav: false,
        controlsContainer: ".control-slide",
        responsive: {
            1025: {
                items: 5
            },
            768: {
                items: 4
            }
        }
    })
}

const btnTrigger = document.getElementById("triggerSearchBar")
const searchBar = document.getElementById("menu-search-bar")
if(btnTrigger) {
    btnTrigger.addEventListener("click", function() {
        searchBar.classList.toggle("display-search-bar")
    })

    document.addEventListener("click", function(e) {
        const isInsideSearch = searchBar.contains(e.target)
        const isInsideBtn = btnTrigger.contains(e.target)
        if(!isInsideSearch && !isInsideBtn) {
            searchBar.classList.remove("display-search-bar")
        }
    })
}

const reviewPoint = document.querySelector(".review-point")
const checkboxs = document.querySelectorAll(".review-point .radio-icon")
if(reviewPoint) {
    checkboxs.forEach((checkbox) => {
        checkbox.addEventListener("click", function() {
            const checked = document.querySelector(".review-point .isChecked")
            checked?.classList.remove("isChecked")
            this.classList.add("isChecked")
        })
    })
}


const allPrices = document.querySelectorAll(".card-price");

allPrices.forEach((element, index)=>{
    let currentPrice = element.firstElementChild;
    let cost = element.lastElementChild;
    let txt = cost.innerHTML;
    if(txt == "") {
        currentPrice.classList.add("cl-text-price");
    }else {
        currentPrice.classList.remove("cl-text-price");
    }
})

const wishlist = document.getElementById("wishlist-content")

if(wishlist) {
    const chooseAll = document.querySelector("#wishlist-content form input[name='select-all']")
    const listChoose = document.querySelectorAll("#wishlist-content tbody .wishlist_item .wishlist-choice input")

    chooseAll.addEventListener("click", function() {
        listChoose.forEach(choose => {
            choose.checked = this.checked
        })
    })
}

var getShowPass = document.getElementById("show-password");
var getIpPass = document.getElementById("ip_password-login");
var getIconShowPass = document.getElementById("show-password");

if (getShowPass) {
    getShowPass.addEventListener("click", showPassword);
    function showPassword() {
        if (getIpPass.getAttribute("type") == "password") {
            getIpPass.setAttribute("type", "text");
            getIconShowPass.innerHTML = '<i class="bi bi-eye"></i>';
        } else {
            getIpPass.setAttribute("type", "password");
            getIconShowPass.innerHTML = '<i class="bi bi-eye-slash"></i>';
        }
    }
}

function  changeMin(e){
    let newValue = parseInt(e.target.value);
    let filterMin = document.getElementById("state-thumb-left");
    if(newValue > max){
        e.target.value = max;
        newValue = max;
    }

    filterMin.style.left = calcLeftPosition(newValue) + '%';
    document.getElementById("min-price").innerHTML = newValue + ' VNĐ';
    getSlider.style.left = calcLeftPosition(newValue) + '%';
    getSlider.style.right = (100 - calcLeftPosition(max)) + '%';
    setIndex(min, e.target.max, ipRangeMin);
}

function  changeMax(e) {
    let newValue = parseInt(e.target.value);
    let filterMax = document.getElementById("state-thumb-right");
    if(newValue < min) {
        e.target.value = min;
        newValue = min;
    }

    filterMax.style.left = calcLeftPosition(newValue) + '%';
    document.getElementById("max-price").innerHTML = newValue + ' VNĐ';
    getSlider.style.left = calcLeftPosition(min) + '%';
    getSlider.style.right = (100 - calcLeftPosition(newValue))+ '%';
    setIndex(max, e.target.min, ipRangeMax);
}

function setRange() {

    let ipRangeMin = document.getElementById("range-min");
    let ipRangeMax = document.getElementById("range-max");
    if(!ipRangeMin || !ipRangeMax) return;
    let getSlider = document.getElementById("slider-range");
    let min = ipRangeMin.getAttribute("min");
    let max = ipRangeMax.getAttribute("max");
    const calcLeftPosition = value => (100 / (max - min))*(value - min);

    ipRangeMin.addEventListener("input",function (e){
        changeMin(e);
    })
    ipRangeMax.addEventListener("input",function (e){
        changeMax(e);
    })
}
setRange()

function setIndex(value,valueTarget, element) {
    if(value == valueTarget){
        element.style.zIndex = '4';
    }else {
        element.style.zIndex = '3';
    }
}

