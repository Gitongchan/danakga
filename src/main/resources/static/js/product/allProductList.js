const name = `<div className="col-lg-3 col-md-6 col-12">

    <!-- Start Single Product -->
    <div className="single-product">
        <div className="product-image">
            <img src="https://via.placeholder.com/335x335" alt="#">
                <div className="button">
                    <a href="product-details.mustache" className="btn"><i className="lni lni-cart"></i> Add to Cart</a>
                </div>
        </div>
        <div className="product-info">
            <span className="category">Watches</span>
            <h4 className="title">
                <a href="product-grids.mustache">Xiaomi Mi Band 5</a>
            </h4>
            <ul className="review">
                <li><i className="lni lni-star-filled"></i></li>
                <li><i className="lni lni-star-filled"></i></li>
                <li><i className="lni lni-star-filled"></i></li>
                <li><i className="lni lni-star-filled"></i></li>
                <li><i className="lni lni-star"></i></li>
                <li><span>4.0 Review(s)</span></li>
            </ul>
            <div className="price">
                <span>$199.00</span>
            </div>
        </div>
    </div>
    <!-- End Single Product -->
</div>`

fetch('/api/product/main-page/list/productOrderCount?page=0')
    .then((res)=>res.json())
    .then((data)=>{

    })