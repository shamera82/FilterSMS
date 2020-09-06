package com.sham.filtersms

class FilterSMS {
    var filterKeyword = ""
    var replySMS = ""
    constructor(filterKeyword: String, replySMS: String){
        this.filterKeyword = filterKeyword
        this.replySMS = replySMS
    }
}

//class FilterSMS(val filterKeyword: String, val replySMS: String)