package ac.hurley.model.model

import ac.hurley.model.room.entity.Article

data class ArticleListModel(
    var curPage: Int,
    var datas: List<Article>,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)