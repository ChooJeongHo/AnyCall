package com.example.anycall

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
        TODO("Not yet implemented")
    }
}