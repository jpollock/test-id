package com.example.domain

import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.akkaserverless.scalasdk.valueentity.ValueEntityContext
import com.example
import com.google.protobuf.empty.Empty

// This class was initially generated based on the .proto definition by Akka Serverless tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

/** A value entity. */
class Counter(context: ValueEntityContext) extends AbstractCounter {
  override def emptyState: CounterState = CounterState()

  override def create(currentState: CounterState, command: example.CreateCounter): ValueEntity.Effect[example.CurrentCounter] =
    if (command.initialValue < 0)
      effects.error(s"Cannot set a negative starting number. Passed in data was: [${command.initialValue}].")
    else {
      val newState = currentState.copy(value = command.initialValue)
      effects
        .updateState(newState)
        .thenReply(example.CurrentCounter(counterId= context.entityId, value = newState.value))
    }

  override def increase(currentState: CounterState, command: example.IncreaseValue): ValueEntity.Effect[Empty] =
    if (command.value < 0)
      effects.error(s"Increase requires a positive value. It was [${command.value}].")
    else {
      val newState = currentState.copy(value = currentState.value + command.value)
      effects
        .updateState(newState)
        .thenReply(Empty.defaultInstance)
    }

  override def decrease(currentState: CounterState, decreaseValue: example.DecreaseValue): ValueEntity.Effect[Empty] =
    if (command.value < 0)
      effects.error(s"Decreate requires a positive value. It was [${command.value}].")
    else {
      val newState = currentState.copy(value = currentState.value - command.value)
      effects
        .updateState(newState)
        .thenReply(Empty.defaultInstance)
    }
    
  override def reset(currentState: CounterState, resetValue: example.ResetValue): ValueEntity.Effect[Empty] =
    val newState = currentState.copy(value = currentState.value + command.value)
      effects
        .updateState(newState)
        .thenReply(Empty.defaultInstance)


  override def getCurrentCounter(currentState: CounterState,command: example.GetCounter): ValueEntity.Effect[example.CurrentCounter] =
    effects.reply(example.CurrentCounter(counterId= context.entityId, currentState.value))

}

