package com.koalatea.sedaily.feature.commentList.epoxy

import android.widget.Button
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.koalatea.sedaily.R

@EpoxyModelClass(layout = R.layout.view_holder_comment)
abstract class CommentEpoxyModelWithHolder : BaseCommentEpoxyModelWithHolder<CommentHolder>() {

    @EpoxyAttribute lateinit var replyClickListener: () -> Unit
    @EpoxyAttribute
    lateinit var upvoteClickListener: () -> Unit

    override fun bind(holder: CommentHolder) {
        super.bind(holder)

        holder.likesButton.setOnClickListener {
            it.isEnabled = false
            upvoteClickListener()
        }

        holder.replayButton.setOnClickListener { replyClickListener() }
    }

}

class CommentHolder : BaseCommentHolder() {
    val likesButton by bind<Button>(R.id.likesButton)
    val replayButton by bind<Button>(R.id.replyButton)
}