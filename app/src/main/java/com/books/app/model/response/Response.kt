package com.books.app.model.response

data class Response(
    val books: List<Book>,
    val top_banner_slides: List<TopBannerSlide>,
    val you_will_like_section: List<Long>
)