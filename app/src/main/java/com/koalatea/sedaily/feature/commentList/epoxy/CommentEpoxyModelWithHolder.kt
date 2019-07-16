package com.koalatea.sedaily.feature.commentList.epoxy

import android.widget.Button
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.button.MaterialButton
import com.koalatea.sedaily.R

@EpoxyModelClass(layout = R.layout.view_holder_comment)
abstract class CommentEpoxyModelWithHolder : BaseCommentEpoxyModelWithHolder<CommentHolder>() {

    @EpoxyAttribute lateinit var replyClickListener: () -> Unit

    @EpoxyAttribute
    var upvoted: Boolean? = null
    @EpoxyAttribute
    var score: Int? = null
    @EpoxyAttribute
    lateinit var upvoteClickListener: () -> Unit

    override fun bind(holder: CommentHolder) {
        super.bind(holder)

        holder.replayButton.setOnClickListener { replyClickListener() }

        holder.likesButton.setIconResource(if (upvoted == true) R.drawable.vd_favorite else R.drawable.vd_favorite_border)
        holder.likesButton.text = score?.let {
            if (it > 0) {
                it.toString()
            } else {
                ""
            }
        }
        holder.likesButton.setOnClickListener { upvoteClickListener() }
    }

}

class CommentHolder : BaseCommentHolder() {
    val replayButton by bind<Button>(R.id.replyButton)
    val likesButton by bind<MaterialButton>(R.id.likesButton)
}