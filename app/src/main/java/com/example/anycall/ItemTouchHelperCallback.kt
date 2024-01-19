package com.example.anycall

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// ItemTouchHelper.Callback 클래스를 상속
class ItemTouchHelperCallback(val listener: ItemTouchHelperListener)
    : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
    // 활성화된 이동 방향을 정의하는 플래그를 반환하는 메소드
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 드래그 방향
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        // 스와이프 방향
        val swipeFlags = ItemTouchHelper.RIGHT
        // 이동을 만드는 메소드
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    // 사용자에 의해 swipe될 때 호출
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 리스너의 onItemSwipe 메소드 호출
        listener.onItemSwipe(viewHolder.adapterPosition)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }
    // 아이템을 스와이프할 때 호출되는 메서드
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // 스와이프할 때 배경색을 변경하기 위한 Paint 객체 생성
        val paint = Paint()
        paint.color = Color.parseColor("#FFAE10")

        // 스와이프한 영역을 그리기
        c.drawRect(
            viewHolder.itemView.left.toFloat(),
            viewHolder.itemView.top.toFloat(),
            viewHolder.itemView.right + dX,
            viewHolder.itemView.bottom.toFloat(),
            paint
        )

        // 원래의 onChildDraw 메서드 호출 (이 부분을 지우지 않도록 주의)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}