package aiac

default allow = false

allow if {
    input.agentId != ""
    lower(input.role) == "admin"
}

allow if {
    input.agentId != ""
    lower(input.role) == "analyst"
    lower(input.resource) == "financial_report"
    lower(input.action) == "read"
}
