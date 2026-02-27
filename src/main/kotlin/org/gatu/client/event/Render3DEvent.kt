package org.gatu.client.event

import org.gatu.client.event.Event
import org.joml.Matrix4f

class Render3DEvent(val matrix: Matrix4f) : Event()