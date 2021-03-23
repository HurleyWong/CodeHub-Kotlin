package ac.hurley.model.model

data class CollectListModel(
    val curPage: Int,
    val datas: List<CollectionModel>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)