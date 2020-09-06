package com.sham.filtersms

class FilterSMS {
    var filterKeyword = ""
    var replySMS = ""
    var sirenName=""
    constructor(filterKeyword: String, replySMS: String, sirenName: String){
        this.filterKeyword = filterKeyword
        this.replySMS = replySMS
        this.sirenName = sirenName
    }
}

//class FilterSMS(val filterKeyword: String, val replySMS: String)