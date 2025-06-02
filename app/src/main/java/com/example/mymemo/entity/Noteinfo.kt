package com.example.mymemo.entity

import java.io.Serializable

class Noteinfo : Serializable {

    var note_id: Int = 0
    private var username: String = ""
    private var note_title: String = ""
    private var note_content: String = ""
    private var note_create_time: String = ""
    var note_is_top: Int = 0

    constructor()

    constructor(
        note_id: Int,
        username: String,
        note_title: String,
        note_content: String,
        note_create_time: String,
        note_is_top: Int
    ) {
        this.note_id = note_id
        this.username = username
        this.note_title = note_title
        this.note_content = note_content
        this.note_create_time = note_create_time
        this.note_is_top = note_is_top
    }

    fun getNoteId(): Int {
        return note_id
    }

    fun setNoteId(note_id: Int) {
        this.note_id = note_id
    }

    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getNoteTitle(): String {
        return note_title
    }

    fun setNoteTitle(note_title: String) {
        this.note_title = note_title
    }

    fun getNoteContent(): String {
        return note_content
    }

    fun setNoteContent(note_content: String) {
        this.note_content = note_content
    }

    fun getNoteCreateTime(): String {
        return note_create_time
    }

    fun setNoteCreateTime(note_create_time: String) {
        this.note_create_time = note_create_time
    }

    fun getNoteIsTop(): Int {
        return note_is_top
    }

    fun setNoteIsTop(note_is_top: Int) {
        this.note_is_top = note_is_top
    }
}
