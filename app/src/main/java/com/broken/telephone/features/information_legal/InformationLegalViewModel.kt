package com.broken.telephone.features.information_legal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.information_legal.model.InformationLegalSideEffect
import com.broken.telephone.features.information_legal.use_case.GetPrivacyPolicyLinkUseCase
import com.broken.telephone.features.information_legal.use_case.GetTermsOfServiceLinkUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class InformationLegalViewModel(
    private val getTermsOfServiceLinkUseCase: GetTermsOfServiceLinkUseCase,
    private val getPrivacyPolicyLinkUseCase: GetPrivacyPolicyLinkUseCase,
) : ViewModel() {

    private val _sideEffects = Channel<InformationLegalSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onTermsOfServiceClick() {
        viewModelScope.launch {
            _sideEffects.send(InformationLegalSideEffect.OpenLink(getTermsOfServiceLinkUseCase()))
        }
    }

    fun onPrivacyPolicyClick() {
        viewModelScope.launch {
            _sideEffects.send(InformationLegalSideEffect.OpenLink(getPrivacyPolicyLinkUseCase()))
        }
    }
}
